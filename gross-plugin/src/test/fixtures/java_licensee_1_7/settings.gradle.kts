pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        maven(url = "file://${settingsDir.absolutePath}/repo")
        mavenCentral()
        google()
    }
}

include(":")