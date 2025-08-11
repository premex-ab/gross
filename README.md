# Gross - Gradle Open Source Software

[![Plugin Portal](https://img.shields.io/gradle-plugin-portal/v/se.premex.gross)](https://plugins.gradle.org/plugin/se.premex.gross)
[![License](https://img.shields.io/github/license/premex-ab/gross)](LICENSE)

Gross is a Gradle plugin that helps you manage and document open source licenses in your Android and Kotlin projects. It works in conjunction with [CashApp's Licensee plugin](https://github.com/cashapp/licensee) to generate useful artifacts for license compliance.

## Features

- **Kotlin Code Generation**: Generates a static list of open source artifacts with their licenses as Kotlin code
- **Android Asset Generation**: Saves the licensee report as an Android asset for runtime access
- **License Compliance**: Makes it easier to comply with open source license requirements in your projects

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

## Generated Sources Reference

When using the Kotlin code generation feature (`enableKotlinCodeGeneration`), Gross generates Kotlin classes that provide type-safe access to your project's dependencies and their licenses. This section explains how to use the generated sources.

### Generated Classes Structure

The plugin generates the following classes in the `se.premex.gross` package:

- **`Gross`**: A Kotlin object that contains the static list of artifacts
  - `artifacts`: A `List<Artifact>` containing all your project's dependencies

- **`Artifact`**: A data class representing a single dependency
  - `groupId`: The Maven group ID (String)
  - `artifactId`: The artifact ID (String)
  - `version`: The version (String)
  - `name`: The human-readable name of the artifact (String?)
  - `spdxLicenses`: A list of standard SPDX licenses (List<SpdxLicenses>)
  - `scm`: Source control management information (Scm?)
  - `unknownLicenses`: Any licenses not conforming to SPDX standard (List<UnknownLicenses>)

- **`SpdxLicenses`**: A data class representing a standard SPDX license
  - `identifier`: The SPDX identifier (e.g., "Apache-2.0") (String)
  - `name`: The full name of the license (String)
  - `url`: The URL to the license text (String)

- **`Scm`**: A data class containing source control information
  - `url`: The URL to the source repository (String)

- **`UnknownLicenses`**: A data class for non-standard licenses
  - `name`: The name of the license (String)
  - `url`: The URL to the license text (String)

### Accessing Generated Sources

The generated sources are available after the Gradle build process completes. To use them in your code:

1. Make sure you have `enableKotlinCodeGeneration.set(true)` in your `gross` configuration.
2. Build your project to generate the Kotlin classes.
3. Import and use the `Gross.artifacts` object in your code.

### Common Use Cases

#### Basic Usage: Listing All Dependencies

```kotlin
// Access the list of artifacts
val artifacts = Gross.artifacts

// Print information about each artifact
artifacts.forEach { artifact ->
    println("${artifact.artifactId} (${artifact.groupId}:${artifact.version})")
    
    // Print SPDX licenses if available
    artifact.spdxLicenses?.forEach { license ->
        println("  - ${license.name} (${license.identifier}): ${license.url}")
    }
    
    // Print unknown licenses if available
    artifact.unknownLicenses?.forEach { license ->
        println("  - ${license.name}: ${license.url}")
    }
}
```

#### Filtering Artifacts by License Type

```kotlin
// Find all artifacts with Apache-2.0 license
val apacheLicensed = Gross.artifacts.filter { artifact ->
    artifact.spdxLicenses?.any { it.identifier == "Apache-2.0" } == true
}

// Find artifacts without a license
val unlicensed = Gross.artifacts.filter { artifact ->
    (artifact.spdxLicenses == null || artifact.spdxLicenses.isEmpty()) &&
    (artifact.unknownLicenses == null || artifact.unknownLicenses.isEmpty())
}
```

#### Finding a Specific Dependency

```kotlin
// Find a specific artifact by coordinates
val kotlin = Gross.artifacts.firstOrNull { 
    it.groupId == "org.jetbrains.kotlin" && it.artifactId == "kotlin-stdlib" 
}

kotlin?.let {
    println("Kotlin Standard Library version: ${it.version}")
    println("License: ${it.spdxLicenses?.firstOrNull()?.name}")
}
```

#### Creating UI Components

See the [Usage Examples](#usage-examples) section for examples of creating UI components to display license information.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
