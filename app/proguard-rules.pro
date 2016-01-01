# Retrofit
-dontwarn retrofit.**
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions

# OkHttp
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# Realm
-keep class io.realm.annotations.RealmModule
-keep @io.realm.annotations.RealmModule class *
-keep class io.realm.internal.Keep
-keep @io.realm.internal.Keep class * { *; }
-dontwarn javax.**
-dontwarn io.realm.**

# Appcompat
-keep public class android.support.v7.widget.** { *; }
-keep public class android.support.v7.internal.widget.** { *; }
-keep public class android.support.v7.internal.view.menu.** { *; }
-keep public class * extends android.support.v4.view.ActionProvider {
    public <init>(android.content.Context);
}

# Hockey App
-keep public class javax.net.ssl.**
-keep public class org.apache.http.**
-keepclassmembers public class javax.net.ssl.** { *; }
-keepclassmembers public class org.apache.http.** { *; }
-keepclassmembers class net.hockeyapp.android.UpdateFragment { *; }
-dontwarn org.apache.**
-dontwarn net.hockeyapp.android.FeedbackManager
-dontwarn net.hockeyapp.android.tasks.ParseFeedbackTask

# Misc
-dontwarn okio.**