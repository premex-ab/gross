# Gross - Gradle Open Source Software

[![Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/se.premex.gross)](https://plugins.gradle.org/plugin/se.premex.gross)
[![License](https://img.shields.io/github/license/premex-ab/gross)](LICENSE)

Gross is a Gradle plugin that helps you manage and document open source licenses in your Android and Kotlin projects. It works in conjunction with [CashApp's Licensee plugin](https://github.com/cashapp/licensee) to generate useful artifacts for license compliance.

## Features

- **Kotlin Code Generation**: Generates a static list of open source artifacts with their licenses as Kotlin code
- **Android Asset Generation**: Saves the licensee report as an Android asset for runtime access
- **License Compliance**: Makes it easier to comply with open source license requirements in your projects
- **Core Library**: Provides data classes for artifacts and licenses in a separate library that can be used by other modules

## Installation

Gross requires both the [Licensee plugin](https://github.com/cashapp/licensee) and the Gross plugin. Add the following to your project:

### In settings.gradle.kts:

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}
```

### In build.gradle.kts (root project):

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("app.cash.licensee:licensee-gradle-plugin:1.11.0")
    }
}

plugins {
    id("app.cash.licensee")
    id("se.premex.gross") version "0.1.0"
}
```

## Configuration

Configure Gross in your `build.gradle.kts` file:

```kotlin
gross {
    // Enable generation of Kotlin code for artifacts
    enableKotlinCodeGeneration.set(true) 
    
    // Enable generation of Android assets
    enableAndroidAssetGeneration.set(true)
    
    // Optional: Customize the name of the generated Android asset
    androidAssetFileName.set("licenses.json")
}
```

## Features Explained

### Kotlin Code Generation

When `enableKotlinCodeGeneration` is enabled, Gross generates a static list of open source artifacts with their licenses as Kotlin code. This is available through the `Gross.artifacts` class, which you can use in your application to display license information.

Example usage:

```kotlin
// Access your dependencies and their licenses
val licenses = Gross.artifacts
for (artifact in licenses) {
    println("${artifact.name} - ${artifact.license}")
}
```

### Android Asset Generation

When `enableAndroidAssetGeneration` is enabled, Gross saves the licensee report as an Android asset. By default, this file is named `artifacts.json` but you can customize the name using the `androidAssetFileName` property.

You can use `AssetLicenseParser` to read and parse this file at runtime:

```kotlin
val licenses = AssetLicenseParser(context).parse()
```

### Core Library

The core module provides the data classes (`Artifact`, `SpdxLicenses`, `Scm`, `UnknownLicenses`) that can be used by other modules in your project. To use them, you just need to add a dependency to the core module:

```kotlin
dependencies {
    implementation("se.premex.gross:core:1.0")
}
```

## Usage Examples

### Creating a licenses screen in an Android app with Jetpack Compose

```kotlin
@Composable
fun LicensesScreen() {
    LazyColumn {
        items(Gross.artifacts) { artifact ->
            LicenseItem(artifact)
        }
    }
}

@Composable
fun LicenseItem(artifact: Gross.Artifact) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = artifact.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = artifact.license ?: "No license specified",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
