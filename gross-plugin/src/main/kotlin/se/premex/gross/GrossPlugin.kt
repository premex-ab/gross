package se.premex.gross

import app.cash.licensee.LicenseeTask
import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.reporting.ReportingExtension
import org.gradle.kotlin.dsl.register
import java.io.File
import java.util.Locale

private enum class AndroidPlugin {
    Application,
    Library,
    DynamicFeature,
}

class GrossPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create("gross", GrossExtension::class.java)
        extension.enableKotlinCodeGeneration.convention(true)
        extension.enableAndroidAssetGeneration.convention(false)
        extension.androidAssetFileName.convention("artifacts.json")

        val reportingExtension: ReportingExtension =
            project.extensions.getByType(ReportingExtension::class.java)

        val androidPlugin = if (project.plugins.hasPlugin("com.android.application")) {
            AndroidPlugin.Application
        } else if (project.plugins.hasPlugin("com.android.library")) {
            AndroidPlugin.Library
        } else if (project.plugins.hasPlugin("com.android.dynamic-feature")) {
            AndroidPlugin.DynamicFeature
        } else {
            null
        }

        if (androidPlugin != null) {
            configureAndroidPlugin(project, extension, reportingExtension)
        }
    }

    private fun configureAndroidPlugin(
        project: Project,
        extension: GrossExtension,
        reportingExtension: ReportingExtension
    ) {
        val androidComponentsExtension =
            project.extensions.getByName("androidComponents") as ApplicationAndroidComponentsExtension

        androidComponentsExtension.onVariants { variant ->
            val capitalizedVariantName = variant.name.replaceFirstChar {
                if (it.isLowerCase()) {
                    it.titlecase(Locale.getDefault())
                } else {
                    it.toString()
                }
            }

            val licensee17ArtifactsFile = reportingExtension.file("licensee/${variant.name}/artifacts.json")
            val licensee18ArtifactsFile =
                reportingExtension.file("licensee/android$capitalizedVariantName/artifacts.json")

            if (extension.enableAndroidAssetGeneration.get()) {
                val copyArtifactsTask =
                    project.tasks.register<AssetCopyTask>("copy${capitalizedVariantName}LicenseeReportToAssets") {
                        targetFileName.set(extension.androidAssetFileName.get())

                        project.tasks.withType(LicenseeTask::class.java)
                            .findByName("licenseeAndroid$capitalizedVariantName")?.let {
                                wireAssetCopyingTask(this, inputFile, it, licensee18ArtifactsFile)
                            }
                        project.tasks.withType(LicenseeTask::class.java)
                            .findByName("licensee$capitalizedVariantName")?.let {
                                wireAssetCopyingTask(this, inputFile, it, licensee17ArtifactsFile)
                            }
                    }

                variant.sources.assets!!.addGeneratedSourceDirectory(
                    copyArtifactsTask,
                    AssetCopyTask::outputDirectory
                )
            }

            if (extension.enableKotlinCodeGeneration.get()) {
                val codeGenerationTask =
                    project.tasks.register<CodeGenerationTask>("${capitalizedVariantName}LicenseeReportToKotlin") {
                        project.tasks.withType(LicenseeTask::class.java)
                            .findByName("licenseeAndroid$capitalizedVariantName")?.let {
                                wireCodeGenerationTask(this, inputFile, it, licensee18ArtifactsFile)
                            }
                        project.tasks.withType(LicenseeTask::class.java)
                            .findByName("licensee$capitalizedVariantName")?.let {
                                wireCodeGenerationTask(this, inputFile, it, licensee17ArtifactsFile)
                            }
                    }

                variant.sources.java!!.addGeneratedSourceDirectory(
                    codeGenerationTask,
                    CodeGenerationTask::outputDirectory
                )
            }
        }
    }

    private fun wireCodeGenerationTask(
        task: CodeGenerationTask,
        inputFile: RegularFileProperty,
        licenseeTask: LicenseeTask,
        licenseeArtifactsFile: File
    ) {
        try {
            // licensee 1.11 + behavior
            inputFile.set(licenseeTask.jsonOutput)
        } catch (_: NoSuchMethodError) {
            // licensee 1.7/1.8 behavior - jsonOutput was introduced in 1.11
            inputFile.set(licenseeArtifactsFile)
            task.dependsOn(licenseeTask)
        }
    }

    private fun wireAssetCopyingTask(
        task: AssetCopyTask,
        inputFile: RegularFileProperty,
        licenseeTask: LicenseeTask,
        licenseeArtifactsFile: File
    ) {
        try {
            // licensee 1.11 + behavior
            inputFile.set(licenseeTask.jsonOutput)
        } catch (_: NoSuchMethodError) {
            // licensee 1.7/1.8 behavior - jsonOutput was introduced in 1.11
            inputFile.set(licenseeArtifactsFile)
            task.dependsOn(licenseeTask)
        }
    }
}
