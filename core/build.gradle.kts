// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    kotlin("jvm") version "2.1.21"
    alias(libs.plugins.org.jetbrains.kotlin.plugin.serialization)
    alias(libs.plugins.io.gitlab.arturbosch.detekt)

}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
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
    implementation(platform(libs.org.jetbrains.kotlinx.kotlinx.serialization.bom))
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json.okio)
    implementation(libs.com.squareup.okio)

    testImplementation(libs.org.jetbrains.kotlin.kotlin.test.junit)
    testImplementation(libs.org.jetbrains.kotlinx.kotlinx.coroutines.test)

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}
