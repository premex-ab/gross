plugins {
    id("java")
    id("se.premex.gross")
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("app.cash.licensee:licensee-gradle-plugin:1.7.0")
    }
}

apply(plugin = "app.cash.licensee")

dependencies {
    implementation("com.example:example:1.0.0")
}

//licensee {
//    allow("Apache-2.0")
//}
