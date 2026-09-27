# قواعد Proguard لتطبيق عمران
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

# نماذج Firestore تحتاج البناء الانعكاسي
-keep class com.omran.quran.data.model.** { *; }

# Hilt / Dagger
-keep class dagger.hilt.** { *; }
