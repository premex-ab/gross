plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization") version "1.9.21"
    id("java-gradle-plugin")
    id("com.gradle.plugin-publish") version "0.21.0"
    alias(libs.plugins.com.vanniktech.maven.publish)
    id("maven-publish")
    id("com.gladed.androidgitversion") version "0.4.14"
    id("io.gitlab.arturbosch.detekt") version "1.23.4"
}

kotlin {
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

androidGitVersion {
    tagPattern = "^v[0-9]+.*"
}

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
}

buildscript {
    dependencies {
        classpath(libs.com.android.tools.build.gradle)
    }
}

dependencies {
    implementation(libs.com.android.tools.build.gradle)
    implementation(libs.org.jetbrains.kotlin.kotlin.gradle.plugin)
    implementation(libs.com.squareup.kotlinpoet) {
        exclude(module = "kotlin-reflect")
    }
    implementation(platform(libs.org.jetbrains.kotlinx.kotlinx.serialization.bom))
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json)
    implementation(libs.org.jetbrains.kotlinx.kotlinx.serialization.json.okio)
    implementation(libs.com.squareup.okio)
    compileOnly(libs.com.squareup.licensee)

    testImplementation(platform(libs.org.junit.junit.bom))
    testImplementation(libs.org.junit.jupiter.junit.jupiter.api)
    testImplementation(libs.org.junit.jupiter.junit.jupiter.params)
    testImplementation(libs.com.google.truth)

    // https://github.com/gradle/gradle/issues/15383
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.4")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

version = androidGitVersion.name().replace("v", "")
group = "se.premex"

tasks.named("test") {
    (this as Test).systemProperties["grossVersion"] = version
    dependsOn(":publishAllPublicationsToTestingRepository")
}

publishing {
    repositories {
        maven {
            name = "testing"
            url = uri(layout.buildDirectory.dir("localMaven"))
        }
    }
}

gradlePlugin {
    plugins {
        create("gross") {
            id = "se.premex.gross"
            implementationClass = "se.premex.gross.GrossPlugin"
        }
    }
}

pluginBundle {
    website = "https://github.com/premex-ab/gross"
    vcsUrl = "https://github.com/premex-ab/gross.git"
    description = "A plugin that generates a list of open source licenses you depend on"
    tags = mutableListOf("tooling", "open source", "premex")

    (plugins) {
        "gross" {
            displayName = "Generates a list of open source licenses you depend on"
            description =
                """Generates a list of open source licenses you depend on. Depends on the output of licensee from cashapp - https://github.com/cashapp/licensee.
                    |
                    |Can generate a static list or copy licenses to android assets. 
                    |
                    |As licensee supports KMM the plugin could support more platforms but current only supports android.
            """.trimMargin()
        }
    }
}


allprojects {
    tasks.withType<ValidatePlugins>().configureEach {
        enableStricterValidation.set(true)
    }
}