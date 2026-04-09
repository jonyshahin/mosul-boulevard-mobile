# ============================================================
# Mosul Boulevard — release ProGuard / R8 rules
# ============================================================

# Keep stack traces useful for crash reports.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Keep generic signatures and annotations (Retrofit/Moshi reflection).
-keepattributes Signature,InnerClasses,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations,RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

# ------------------------------------------------------------
# Moshi
# ------------------------------------------------------------
-keep class com.squareup.moshi.** { *; }
-keep interface com.squareup.moshi.** { *; }
-keepclassmembers class * {
    @com.squareup.moshi.FromJson <methods>;
    @com.squareup.moshi.ToJson <methods>;
}
# Keep classes annotated @JsonClass and their generated adapters.
-keep @com.squareup.moshi.JsonClass class *
-keep class **JsonAdapter { *; }
-keepnames @com.squareup.moshi.JsonClass class *

# ------------------------------------------------------------
# App DTOs (used by Moshi via reflection / generated adapters)
# ------------------------------------------------------------
-keep class com.mbp.app.data.model.** { *; }
-keepclassmembers class com.mbp.app.data.model.** { *; }

# ------------------------------------------------------------
# Retrofit
# ------------------------------------------------------------
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Exceptions

# Keep service interfaces; Retrofit reads their annotations at runtime.
-keep interface com.mbp.app.data.api.** { *; }

# ------------------------------------------------------------
# OkHttp / Okio
# ------------------------------------------------------------
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ------------------------------------------------------------
# Kotlin coroutines
# ------------------------------------------------------------
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ------------------------------------------------------------
# Hilt / Dagger
# ------------------------------------------------------------
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.** { *; }
-keep,allowobfuscation @interface dagger.hilt.android.AndroidEntryPoint
-keep,allowobfuscation @interface dagger.hilt.InstallIn

# ------------------------------------------------------------
# Coil
# ------------------------------------------------------------
-dontwarn coil.**
-keep class coil.** { *; }

# ------------------------------------------------------------
# Compose / general
# ------------------------------------------------------------
-dontwarn javax.annotation.**
