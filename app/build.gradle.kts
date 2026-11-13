plugins { id("com.android.application"); id("org.jetbrains.kotlin.android") }
android {
    namespace = "fr.tp.contacts"
    compileSdk = 35
    defaultConfig { applicationId = "fr.tp.contacts"; minSdk = 23; targetSdk = 35; versionCode = 1; versionName = "1.0" }
}
dependencies { implementation("androidx.activity:activity-ktx:1.9.3") }
