# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\tools\adt-bundle-windows-x86_64-20131030\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:
-optimizationpasses 5
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}
-keepclassmembers class com.vbea.java21.data.* {
   public *;
}
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends com.vbea.java21.BaseActivity
-keep public class com.vbea.java21.BaseApplication
-keep class cn.bmob.v3.**
#-keep class com.vbea.java21.data.**
-keep class com.vbea.java21.classes.**
-keep class com.tencent.stat.** {* ;}
-keep class com.tencent.mid.** {* ;}
-keepattributes Signature
-keepattributes EnclosingMethod
#apk包内所有class的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从apk中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
-ignorewarnings # 抑制警告
