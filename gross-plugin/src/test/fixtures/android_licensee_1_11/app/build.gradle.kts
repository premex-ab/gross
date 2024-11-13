plugins {
    id("com.android.application")
    id("app.cash.licensee")
    alias(libs.plugins.gross)
}

licensee {
    allow("Apache-2.0")
}

gross {
    enableKotlinCodeGeneration.set(true)
    enableAndroidAssetGeneration.set(true)
}

android {
    namespace = "se.premex.gross"
    compileSdk = 35

    defaultConfig {
        applicationId = "se.premex.gross"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("com.example:example:1.0.0")
}
