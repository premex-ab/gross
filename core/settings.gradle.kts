rootProject.name = "core"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}

plugins {
    id("com.gradle.develocity") version "4.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version("1.0.0")
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree = "yes"
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
 