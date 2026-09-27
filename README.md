# معهد المقرئ — تطبيق إدارة حلقات ومعاهد القرآن الكريم

تطبيق أندرويد أصلي (Kotlin + Jetpack Compose + Clean Architecture / MVVM) لمتابعة الطلاب، الحفظ، السبر، المراجعة، الحضور، وتوليد التقارير الشاملة.

---

## 🛠️ التقنيات المستخدمة

- **Frontend:** Kotlin, Jetpack Compose, Material Design 3, Hilt, Navigation Compose.
- **Backend & Database:** Supabase (PostgreSQL, Supabase Auth, Supabase Storage, Realtime).
- **Architecture:** MVVM + Clean Architecture + Repository Pattern.

---

## ⚙️ إعداد وقاعدة بيانات Supabase

1. أنشئ مشروعاً جديداً في [Supabase](https://supabase.com).
2. افتح **SQL Editor** ونفّذ ملف المخطط الجاهز في جذر المشروع: [`supabase_schema.sql`](supabase_schema.sql).
3. أنشئ Bucket في Storage باسم `students` واجعله عاماً (Public).
4. اضبط الرابط والمفتاح في `SupabaseConfig.kt`.

---

## 👥 نظام الحسابات والصلاحيات

- **مدير المعهد:** ينشئ المعهد عند التسجيل ويحصل على رمز معهد فريد لربط المعلمين.
- **المعلم:** يسجّل برمز المعهد الخاص به لمتابعة طلاب حلقته وإدخال الحفظ والسبر.

---

## 📦 البناء والتشغيل

لبناء نسخة التطوير (Debug):
```bash
.\gradlew.bat assembleDebug
```

لبناء نسخة الإنتاج النهائية (Release):
```bash
.\gradlew.bat assembleRelease
```
