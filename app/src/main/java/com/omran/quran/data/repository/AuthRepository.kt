package com.omran.quran.data.repository

import com.omran.quran.data.model.AppUser
import com.omran.quran.data.model.Institute
import com.omran.quran.data.model.Session
import com.omran.quran.data.model.UserRole
import com.omran.quran.util.SupabaseConfig
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.status.SessionStatus
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Order
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val supabase: SupabaseClient,
) {
    private val _teacherUpdates = MutableSharedFlow<Unit>(replay = 0, extraBufferCapacity = 1)
    val teacherUpdates = _teacherUpdates.asSharedFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val session: Flow<Session> = supabase.auth.sessionStatus.flatMapLatest { status ->
        when (status) {
            is SessionStatus.Authenticated -> {
                val authUser = status.session.user ?: supabase.auth.currentUserOrNull()
                val uid = authUser?.id
                if (uid == null) {
                    flowOf(Session.SignedOut)
                } else {
                    observeUser(uid, authUser.email ?: "").map { user ->
                        Session.SignedIn(user)
                    }
                }
            }
            is SessionStatus.NotAuthenticated -> flowOf(Session.SignedOut)
            is SessionStatus.Initializing -> flowOf(Session.Loading)
            else -> flowOf(Session.SignedOut)
        }
    }

    val currentUser: Flow<AppUser?> = session.map { (it as? Session.SignedIn)?.user }

    fun observeUser(uid: String, fallbackEmail: String = ""): Flow<AppUser> = flow {
        // 1. استخراج الميتاداتا الصادرة من Supabase Auth (المصدر الموثوق)
        val authUser = supabase.auth.currentUserOrNull()
        val userMeta = authUser?.userMetadata
        val metaRole = userMeta?.get("role")?.jsonPrimitive?.contentOrNull
        val metaInstId = userMeta?.get("institute_id")?.jsonPrimitive?.contentOrNull
        val metaInstName = userMeta?.get("institute_name")?.jsonPrimitive?.contentOrNull
        val metaCircleName = userMeta?.get("circle_name")?.jsonPrimitive?.contentOrNull
        val metaCircleGender = userMeta?.get("circle_gender")?.jsonPrimitive?.contentOrNull
        val metaName = userMeta?.get("name")?.jsonPrimitive?.contentOrNull

        var profile: AppUser? = null
        for (attempt in 1..3) {
            profile = runCatching {
                supabase.from(USERS).select {
                    filter { eq("id", uid) }
                }.decodeSingleOrNull<AppUser>()
            }.getOrNull()

            if (profile != null) break
            if (attempt < 3) delay(400)
        }

        // 2. إذا لم يعثر عليه بالـ UID، نبحث بالبريد الإلكتروني لتصحيح المعرف إن وُجد
        val cleanEmail = (fallbackEmail.ifBlank { authUser?.email.orEmpty() }).trim()
        if (profile == null && cleanEmail.isNotBlank()) {
            val byEmail = runCatching {
                supabase.from(USERS).select {
                    filter { eq("email", cleanEmail) }
                }.decodeSingleOrNull<AppUser>()
            }.getOrNull()

            if (byEmail != null) {
                val reconciled = byEmail.copy(userId = uid)
                runCatching {
                    supabase.from(USERS).upsert(reconciled)
                }
                profile = reconciled
            }
        }

        // 3. تحديد الدور الصارم (الفصل التام بين المدير والمعلم)
        val isExplicitTeacher = metaRole.equals(UserRole.TEACHER.name, ignoreCase = true) ||
                (profile != null && profile.circleName.isNotBlank())
        val isExplicitAdmin = metaRole.equals(UserRole.ADMIN.name, ignoreCase = true)

        val resolvedRole = when {
            isExplicitTeacher -> UserRole.TEACHER.name
            isExplicitAdmin -> UserRole.ADMIN.name
            profile != null && profile.role.isNotBlank() -> {
                if (profile.circleName.isNotBlank()) UserRole.TEACHER.name else profile.role
            }
            // قاعدة ذهبية: أي حساب جديد غير محدد هويته يُعتبر معلماً تلقائياً ولا يُعطى صلاحيات مدير أبداً
            else -> UserRole.TEACHER.name
        }

        // 4. معالجة الحساب في حال عدم وجود ملف شخصي أو نقص في البيانات
        if (profile == null || (resolvedRole == UserRole.ADMIN.name && profile.instituteId.isBlank())) {
            if (resolvedRole == UserRole.ADMIN.name) {
                // حساب مدير: يحق له إنشاء معهد
                val instituteId = profile?.instituteId?.ifBlank { null } ?: UUID.randomUUID().toString()
                val instName = profile?.instituteName?.ifBlank { null }
                    ?: metaInstName?.ifBlank { null } ?: "معهد المقرئ"
                val institute = Institute(
                    instituteId = instituteId,
                    name = instName,
                    shortCode = generateCode(),
                    createdAt = System.currentTimeMillis(),
                )
                runCatching { supabase.from(INSTITUTES).upsert(institute) }

                val resolvedName = profile?.name?.ifBlank { null }
                    ?: metaName?.ifBlank { null }
                    ?: cleanEmail.substringBefore("@").ifBlank { "مدير المعهد" }

                val updatedUser = (profile ?: AppUser(userId = uid, email = cleanEmail)).copy(
                    userId = uid,
                    name = resolvedName,
                    email = if (profile?.email.isNullOrBlank()) cleanEmail else profile.email,
                    role = UserRole.ADMIN.name,
                    instituteId = instituteId,
                    instituteName = instName,
                    createdAt = System.currentTimeMillis(),
                )
                runCatching { supabase.from(USERS).upsert(updatedUser) }
                profile = updatedUser
            } else {
                // حساب معلم: لا يُنشئ معهداً جديداً ولا يُعطى دور المدير إطلاقاً
                val instId = profile?.instituteId?.ifBlank { null }
                    ?: metaInstId?.ifBlank { null }
                    ?: ""
                val instName = profile?.instituteName?.ifBlank { null }
                    ?: metaInstName?.ifBlank { null }
                    ?: "معهد المقرئ"
                val teacherName = profile?.name?.ifBlank { null }
                    ?: metaName?.ifBlank { null }
                    ?: cleanEmail.substringBefore("@").ifBlank { "معلم" }

                val teacherProfile = (profile ?: AppUser(userId = uid, email = cleanEmail)).copy(
                    userId = uid,
                    name = teacherName,
                    email = if (profile?.email.isNullOrBlank()) cleanEmail else profile.email,
                    role = UserRole.TEACHER.name, // تثبيت دور المعلم حصراً
                    instituteId = instId,
                    instituteName = instName,
                    circleName = profile?.circleName?.ifBlank { null } ?: metaCircleName.orEmpty(),
                    circleGender = profile?.circleGender?.ifBlank { null } ?: metaCircleGender?.ifBlank { "MALE" } ?: "MALE",
                    createdAt = if (profile != null && profile.createdAt > 0L) profile.createdAt else System.currentTimeMillis(),
                )
                runCatching { supabase.from(USERS).upsert(teacherProfile) }
                profile = teacherProfile
            }
        } else if (profile.role != resolvedRole) {
            // تصحيح فوري إذا كان الحساب مسجلاً خطأً كمدير سابقاً
            val healed = profile.copy(role = resolvedRole)
            runCatching { supabase.from(USERS).upsert(healed) }
            profile = healed
        }

        emit(profile)
    }

    suspend fun signIn(email: String, password: String) {
        supabase.auth.signInWith(Email) {
            this.email = email.trim()
            this.password = password
        }
    }

    suspend fun registerAdmin(name: String, email: String, password: String, instituteName: String) {
        val cleanName = name.trim()
        val cleanEmail = email.trim()
        val cleanInstName = instituteName.trim().ifBlank { "معهد المقرئ" }

        val result = runCatching {
            supabase.auth.signUpWith(Email) {
                this.email = cleanEmail
                this.password = password
                this.data = buildJsonObject {
                    put("role", UserRole.ADMIN.name)
                    put("name", cleanName)
                    put("institute_name", cleanInstName)
                }
            }
        }.getOrNull()

        var uid = result?.id ?: supabase.auth.currentUserOrNull()?.id
        if (uid == null) {
            runCatching {
                supabase.auth.signInWith(Email) {
                    this.email = cleanEmail
                    this.password = password
                }
            }
            uid = supabase.auth.currentUserOrNull()?.id ?: UUID.randomUUID().toString()
        }

        val instituteId = UUID.randomUUID().toString()
        val shortCode = generateCode()

        val institute = Institute(
            instituteId = instituteId,
            name = cleanInstName,
            shortCode = shortCode,
            createdAt = System.currentTimeMillis(),
        )
        runCatching { supabase.from(INSTITUTES).upsert(institute) }

        val user = AppUser(
            userId = uid,
            name = cleanName,
            email = cleanEmail,
            role = UserRole.ADMIN.name,
            instituteId = instituteId,
            instituteName = institute.name,
            createdAt = System.currentTimeMillis(),
        )
        runCatching { supabase.from(USERS).upsert(user) }
    }

    suspend fun createTeacherAccount(
        instituteId: String,
        instituteName: String,
        circleName: String,
        name: String,
        email: String,
        password: String,
        circleGender: String = "MALE",
    ) {
        val cleanEmail = email.trim()
        val cleanPassword = password
        val cleanName = name.trim()
        val cleanCircleName = circleName.trim()
        val cleanGender = circleGender.ifBlank { "MALE" }
        val cleanInstId = instituteId.trim()
        val cleanInstName = instituteName.trim().ifBlank { "معهد المقرئ" }

        val tempClient = createSupabaseClient(
            supabaseUrl = SupabaseConfig.URL,
            supabaseKey = SupabaseConfig.ANON_KEY,
        ) {
            install(Auth)
            install(Postgrest)
        }

        try {
            var authUid: String? = null
            val signUpResult = runCatching {
                tempClient.auth.signUpWith(Email) {
                    this.email = cleanEmail
                    this.password = cleanPassword
                    this.data = buildJsonObject {
                        put("role", UserRole.TEACHER.name)
                        put("name", cleanName)
                        put("institute_id", cleanInstId)
                        put("institute_name", cleanInstName)
                        put("circle_name", cleanCircleName)
                        put("circle_gender", cleanGender)
                    }
                }
            }

            if (signUpResult.isSuccess) {
                authUid = signUpResult.getOrNull()?.id ?: tempClient.auth.currentUserOrNull()?.id
            } else {
                val signInResult = runCatching {
                    tempClient.auth.signInWith(Email) {
                        this.email = cleanEmail
                        this.password = cleanPassword
                    }
                }
                if (signInResult.isSuccess) {
                    authUid = tempClient.auth.currentUserOrNull()?.id
                    runCatching {
                        tempClient.auth.updateUser {
                            this.data = buildJsonObject {
                                put("role", UserRole.TEACHER.name)
                                put("name", cleanName)
                                put("institute_id", cleanInstId)
                                put("institute_name", cleanInstName)
                                put("circle_name", cleanCircleName)
                                put("circle_gender", cleanGender)
                            }
                        }
                    }
                } else {
                    val rawError = signUpResult.exceptionOrNull()?.message.orEmpty()
                    if (rawError.contains("already registered", ignoreCase = true) ||
                        rawError.contains("already in use", ignoreCase = true) ||
                        rawError.contains("EMAIL_EXISTS", ignoreCase = true)
                    ) {
                        val existingInDb = runCatching {
                            supabase.from(USERS).select {
                                filter { eq("email", cleanEmail) }
                            }.decodeSingleOrNull<AppUser>()
                        }.getOrNull()

                        if (existingInDb != null) {
                            authUid = existingInDb.userId
                        } else {
                            throw IllegalStateException("البريد الإلكتروني مسجل مسبقاً بكلمة مرور أخرى. يرجى إدخال كلمة المرور الصحيحة لنفس البريد أو استخدام بريد مختلف.")
                        }
                    } else {
                        throw signUpResult.exceptionOrNull() ?: IllegalStateException("فشل إنشاء حساب المعلم")
                    }
                }
            }

            val finalUid = authUid?.ifBlank { null }
                ?: throw IllegalStateException("تعذر ربط حساب المعلم، يرجى المحاولة مرة أخرى")

            val teacher = AppUser(
                userId = finalUid,
                name = cleanName,
                email = cleanEmail,
                role = UserRole.TEACHER.name,
                instituteId = cleanInstId,
                instituteName = cleanInstName,
                circleName = cleanCircleName,
                circleGender = cleanGender,
                createdAt = System.currentTimeMillis(),
            )

            // حفظ بيانات المعلم في جدول users
            // 1. المحاولة الأولى عبر عميل المعلم نفسه (لتخطي سياسات الأمان RLS auth.uid() = id)
            var saved = runCatching {
                tempClient.from(USERS).upsert(teacher)
            }.isSuccess

            // 2. إذا فشل الحفظ الكامل (ربما بسبب نقص أعمدة circle_name في قاعدة بيانات Supabase)، نحفظ الحقول الأساسية
            if (!saved) {
                val coreData = buildJsonObject {
                    put("id", finalUid)
                    put("name", cleanName)
                    put("email", cleanEmail)
                    put("role", UserRole.TEACHER.name)
                    put("institute_id", cleanInstId)
                    put("institute_name", cleanInstName)
                    put("created_at", System.currentTimeMillis())
                }
                saved = runCatching {
                    tempClient.from(USERS).upsert(coreData)
                }.isSuccess
            }

            // 3. المحاولة عبر عميل المدير (في حال كانت الصلاحيات تتيح ذلك)
            if (!saved) {
                val adminUpsert = runCatching {
                    supabase.from(USERS).upsert(teacher)
                }
                if (adminUpsert.isFailure) {
                    val coreData = buildJsonObject {
                        put("id", finalUid)
                        put("name", cleanName)
                        put("email", cleanEmail)
                        put("role", UserRole.TEACHER.name)
                        put("institute_id", cleanInstId)
                        put("institute_name", cleanInstName)
                        put("created_at", System.currentTimeMillis())
                    }
                    supabase.from(USERS).upsert(coreData)
                }
            }

            _teacherUpdates.tryEmit(Unit)
        } finally {
            runCatching { tempClient.close() }
        }
    }

    suspend fun updateCircle(
        userId: String,
        circleName: String,
        teacherName: String,
        circleGender: String = "MALE",
    ) {
        if (userId.isBlank()) return
        runCatching {
            supabase.from(USERS).update(
                mapOf(
                    "name" to teacherName.trim(),
                    "circle_name" to circleName.trim(),
                    "circle_gender" to circleGender,
                    "role" to UserRole.TEACHER.name, // تأكيد دور المعلم دائماً
                )
            ) {
                filter { eq("id", userId) }
            }
        }
        runCatching {
            supabase.from("students").update(
                mapOf(
                    "teacher_name" to teacherName.trim(),
                    "circle_name" to circleName.trim(),
                    "gender" to circleGender,
                )
            ) {
                filter { eq("teacher_id", userId) }
            }
        }
        _teacherUpdates.tryEmit(Unit)
    }

    suspend fun deleteTeacher(teacherId: String) {
        if (teacherId.isBlank()) return
        supabase.from(USERS).delete {
            filter { eq("id", teacherId) }
        }
        _teacherUpdates.tryEmit(Unit)
    }

    suspend fun signOut() {
        supabase.auth.signOut()
    }

    suspend fun teachers(instituteId: String): List<AppUser> {
        if (instituteId.isBlank()) return emptyList()
        val allInInstitute = runCatching {
            supabase.from(USERS).select {
                filter {
                    eq("institute_id", instituteId)
                }
                order("name", Order.ASCENDING)
            }.decodeList<AppUser>()
        }.getOrDefault(emptyList())

        return allInInstitute.filter { user ->
            user.role == UserRole.TEACHER.name || user.circleName.isNotBlank()
        }.map { user ->
            if (user.role != UserRole.TEACHER.name && user.circleName.isNotBlank()) {
                val healed = user.copy(role = UserRole.TEACHER.name)
                runCatching { supabase.from(USERS).upsert(healed) }
                healed
            } else {
                user
            }
        }
    }

    suspend fun allStaff(instituteId: String): List<AppUser> {
        if (instituteId.isBlank()) return emptyList()
        return supabase.from(USERS).select {
            filter {
                eq("institute_id", instituteId)
            }
            order("name", Order.ASCENDING)
        }.decodeList<AppUser>()
    }

    suspend fun institute(instituteId: String): Institute? {
        if (instituteId.isBlank()) return null
        return supabase.from(INSTITUTES).select {
            filter { eq("id", instituteId) }
        }.decodeSingleOrNull<Institute>()
    }

    suspend fun persistFcmToken(token: String? = null) {
        val uid = supabase.auth.currentUserOrNull()?.id ?: return
        if (token.isNullOrBlank()) return
        runCatching {
            supabase.from(USERS).update(mapOf("fcm_token" to token)) {
                filter { eq("id", uid) }
            }
        }
    }

    private fun generateCode(): String =
        UUID.randomUUID().toString().replace("-", "").take(6).uppercase()

    companion object {
        const val USERS = "users"
        const val INSTITUTES = "institutes"
    }
}
