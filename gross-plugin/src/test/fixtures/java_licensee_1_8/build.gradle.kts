buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("app.cash.licensee:licensee-gradle-plugin:1.8.0")
    }
}

plugins {
    id("java")
//    id("app.cash.licensee")
    id("se.premex.gross") version "1.2.3"
}

apply(plugin = "app.cash.licensee")

dependencies {
    implementation("com.example:example:1.0.0")
}

licensee {
    allow("Apache-2.0")
}
