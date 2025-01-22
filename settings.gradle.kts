pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "app.cash.licensee") {
                // https://github.com/cashapp/licensee/issues/91
                useModule("app.cash.licensee:licensee-gradle-plugin:${requested.version}")
            }
        }
    }
}

plugins {
    id("com.gradle.develocity") version "3.19"
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

develocity {
    buildScan {
        termsOfUseUrl.set("https://gradle.com/terms-of-service")
        termsOfUseAgree = "yes"
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Gross"

includeBuild("gross-plugin")

includeBuild("ui") {
    dependencySubstitution {
        substitute(module("se.premex.gross:ui"))
            .using(project(":"))
    }
}
includeBuild("core")
include(":app")

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
 