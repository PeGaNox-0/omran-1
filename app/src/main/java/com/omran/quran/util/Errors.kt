package com.omran.quran.util

fun Throwable.toArabicMessage(): String {
    val raw = message.orEmpty()
    return when {
        this is IllegalStateException && raw.isNotBlank() && !raw.startsWith("PGRST") ->
            raw
        raw.contains("password is invalid", true) || raw.contains("INVALID_LOGIN", true) ||
            raw.contains("Invalid login credentials", true) ->
            "البريد أو كلمة المرور غير صحيحة"
        raw.contains("user not found", true) || raw.contains("USER_NOT_FOUND", true) ->
            "الحساب غير موجود"
        raw.contains("email address is already", true) || raw.contains("EMAIL_EXISTS", true) ||
            raw.contains("User already registered", true) ->
            "البريد الإلكتروني مستخدم مسبقاً"
        raw.contains("foreign key constraint", true) ->
            "يرجى الانتظار ثانية واحدة لتحديث بيانات المعهد ثم إعادة المحاولة"
        raw.contains("badly formatted", true) -> "صيغة البريد الإلكتروني غير صحيحة"
        raw.contains("network", true) || raw.contains("UNAVAILABLE", true) || raw.contains("ConnectException", true) ->
            "تعذر الاتصال. تحقق من الإنترنت ثم أعد المحاولة"
        raw.contains("PERMISSION_DENIED", true) || raw.contains("permission denied", true) ||
            raw.contains("row-level security", true) ->
            "خطأ في صلاحيات حفظ البيانات (يرجى مراجعة سياسات RLS في Supabase)"
        raw.contains("WEAK_PASSWORD", true) || raw.contains("Password should be", true) ->
            "كلمة المرور ضعيفة. استخدم 6 أحرف على الأقل"
        raw.contains("Could not find the", true) && raw.contains("column", true) ->
            "قاعدة البيانات تحتاج لتحديث الأعمدة في Supabase"
        else -> {
            if (raw.length > 120) "حدث خطأ أثناء حفظ البيانات، يرجى المحاولة مرة أخرى"
            else localizedMessage ?: "حدث خطأ غير متوقع"
        }
    }
}
