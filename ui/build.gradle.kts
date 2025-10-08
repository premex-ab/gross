import java.util.*

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.library)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.io.gitlab.arturbosch.detekt)
    alias(libs.plugins.org.jetbrains.kotlin.plugin.compose)
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
}

android {
    namespace = "se.premex.gross.oss"

    compileSdk = 36
    defaultConfig {
        minSdk = 24
    }
    buildFeatures {
        compose = true
    }
    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

group = "se.premex.gross"
version = "1.0"

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

dependencies {
    api(projects.core)
    implementation(libs.com.squareup.okio)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation.foundation.layout)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.collections.immutable)

    implementation(platform(libs.org.jetbrains.kotlinx.kotlinx.serialization.bom))
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)

    implementation(libs.androidx.compose.material.material.icons.extended)

    implementation(libs.androidx.compose.animation)

    implementation(libs.androidx.compose.ui.ui.tooling)
    implementation(libs.androidx.compose.runtime.runtime.livedata)
    implementation(libs.androidx.compose.ui.ui.text)

    implementation(libs.androidx.core.core.ktx)
    testImplementation(libs.org.jetbrains.kotlin.kotlin.test.junit)
    testImplementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}
