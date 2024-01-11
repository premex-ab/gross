pluginManagement {
    repositories {
        maven(url = "file://${settingsDir.absolutePath}/../../../../build/localMaven")
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs.register("libs") {
        from(files("../../../../../gradle/libs.versions.toml"))
    }
    repositories {
        maven(url = "file://${settingsDir.absolutePath}/repo")
        mavenCentral()
        google()
    }
}

include(":")
