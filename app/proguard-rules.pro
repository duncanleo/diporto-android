# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/share/android-sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Model classes
-keep class me.duncanleo.diporto.model.** { <fields>; }
-keep class me.duncanleo.diporto.network.payload.** { *; }
-keep class me.duncanleo.diporto.network.response.** { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

# AppCompat v7 Support Lib
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }

-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# Moshi
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-keepclassmembers class ** {
  @com.squareup.moshi.FromJson *;
  @com.squareup.moshi.ToJson *;
}

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# Picasso
-dontwarn com.squareup.okhttp.**

# Kotlin reflect
-keep class org.jetbrains.kotlin.** { *; }
-keep class org.jetbrains.annotations.** { *; }
-keepclassmembers class ** {
  @org.jetbrains.annotations.ReadOnly public *;
}
-dontwarn kotlin.**
-keepattributes Signature
-keepattributes *Annotation*

# CarouselView
-keep class com.synnapps.carouselview.** { *; }

-keepclassmembers class * implements android.os.Parcelable {
  public static final <fields>;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-dontobfuscate
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!code/allocation/variable
