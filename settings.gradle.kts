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
    id("com.gradle.enterprise") version "3.17.5"
    id("org.gradle.toolchains.foojay-resolver-convention") version ("0.8.0")
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}




dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
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
 