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
        val grossVersion: String by settings
        plugin("gross", "se.premex.gross").version(grossVersion)
    }
    repositories {
        maven(url = "file://${settingsDir.absolutePath}/repo")
        mavenCentral()
        google()
    }
}

rootProject.name = "My Application"
include(":app")
 