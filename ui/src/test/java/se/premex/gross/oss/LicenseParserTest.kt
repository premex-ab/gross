package se.premex.gross.oss

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okio.buffer
import okio.source
import se.premex.gross.core.LicenseParser
import kotlin.test.Test

private const val ARTIFACTS_SMALL = """[
    {
        "groupId": "androidx.activity",
        "artifactId": "activity",
        "version": "1.2.3",
        "name": "Activity",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    }
]"""

private const val ARTIFACTS_MEDIUM = """[
    {
        "groupId": "androidx.activity",
        "artifactId": "activity",
        "version": "1.2.3",
        "name": "Activity",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    },
    {
        "groupId": "androidx.activity",
        "artifactId": "activity-compose",
        "version": "1.2.3",
        "name": "Activity Compose",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    },
    {
        "groupId": "androidx.activity",
        "artifactId": "activity-ktx",
        "version": "1.2.3",
        "name": "Activity Kotlin Extensions",
        "spdxLicenses": [
            {
                "identifier": "Apache-2.0",
                "name": "Apache License 2.0",
                "url": "https://www.apache.org/licenses/LICENSE-2.0"
            }
        ],
        "scm": {
            "url": "https://cs.android.com/androidx/platform/frameworks/support"
        }
    }
]"""

class LicenseParserTest {
    @Test
    fun testSmall() = runTest {
        LicenseParser.decode(ARTIFACTS_SMALL.byteInputStream().source().buffer())
    }

    @Test
    fun testMedium() = runTest {
        LicenseParser.decode(ARTIFACTS_MEDIUM.byteInputStream().source().buffer())
    }
}
