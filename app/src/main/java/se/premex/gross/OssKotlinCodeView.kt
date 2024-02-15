package se.premex.gross

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import se.premex.gross.ui.OssView
import se.premex.gross.ui.ViewArtifact
import se.premex.gross.ui.ViewLicense

@Composable
fun ProgrammaticOssView() {
    Column {
        Text(text = stringResource(id = R.string.programmatic))
        val viewData: List<ViewArtifact> = Gross.artifacts.map { artifact ->
            artifact.toViewArtifact()
        }.sortedBy { it.name }

        val groupedArtifacts = viewData
            .groupBy { it.name[0].uppercaseChar().toString() }
            .mapValues { it.value.toImmutableList() }
            .toImmutableMap()

        OssView(groupedArtifacts)
    }
}


internal fun List<UnknownLicenses>?.unknownToLicenses(): List<ViewLicense> =
    orEmpty().map { unknown -> unknown.asLicense() }

internal fun UnknownLicenses.asLicense(): ViewLicense =
    ViewLicense(title = name, identifier = null, url = url)

internal fun List<SpdxLicenses>?.spdxToLicenses(): List<ViewLicense> =
    orEmpty().map { spdx -> spdx.asLicense() }

internal fun SpdxLicenses.asLicense() =
    ViewLicense(title = name, identifier = identifier, url = url)

fun Artifact.toViewArtifact(): ViewArtifact {
    val licenses = spdxLicenses.spdxToLicenses() + unknownLicenses.unknownToLicenses()

    val coordinate = ("$groupId:$artifactId:$version")
    val nameOrArtifactId = name ?: artifactId

    return ViewArtifact(nameOrArtifactId, coordinate, licenses.toImmutableList())
}
