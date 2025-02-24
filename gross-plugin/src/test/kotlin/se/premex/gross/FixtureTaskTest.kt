package se.premex.gross

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.io.File

private val fixturesDir = File("src/test/fixtures")

/**
 * We can't use withPluginClasspath:
 * https://github.com/gradle/gradle/issues/22466
 */
class FixtureTaskTest {

    @ParameterizedTest
    @ValueSource(
        strings = [
//            "java_licensee_1_7",
//            "java_licensee_1_8",
            "android_licensee_1_7",
            "android_licensee_1_8",
            "android_licensee_1_11",
            "android_licensee_1_12",
        ]
    )
    fun checkDescriptionOfTasks(input: String) {
        val fixtureDir = File(fixturesDir, input)

        @Suppress("UNUSED_VARIABLE")
        val result = createRunner(fixtureDir).build()
    }

    private fun createRunner(fixtureDir: File): GradleRunner {
        val gradleRoot = File(fixtureDir, "gradle").also { it.mkdir() }
        File("../gradle/wrapper").copyRecursively(File(gradleRoot, "wrapper"), true)

        val androidSdkFile = File("../local.properties")
        if (androidSdkFile.exists()) {
            androidSdkFile.copyTo(File(fixtureDir, "local.properties"), overwrite = true)
        }

        return GradleRunner.create()
            .withProjectDir(fixtureDir)
            .withDebug(true) // Run in-process
            .withArguments(
                "licensee",
                "copyDebugLicenseeReportToAssets",
                "debugLicenseeReportToKotlin",
                "--stacktrace",
                versionProperty
            )
            // .withPluginClasspath() // https://github.com/gradle/gradle/issues/22466
            .forwardOutput()
    }
}

private val versionProperty = "-PgrossVersion=${System.getProperty("grossVersion")!!}"
