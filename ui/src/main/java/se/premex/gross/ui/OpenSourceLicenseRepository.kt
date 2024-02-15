package se.premex.gross.ui

import android.content.Context
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import se.premex.gross.core.Artifact
import se.premex.gross.core.SpdxLicenses
import se.premex.gross.core.UnknownLicenses

class OpenSourceLicenseRepository(context: Context) {
    private val licenseParser = AssetLicenseParser(context.assets)

    suspend fun getGroupedArtifacts(): ImmutableMap<String, ImmutableList<ViewArtifact>> {
        val artifacts = licenseParser.readFromAssets()

        val viewData: List<ViewArtifact> = artifacts.map { artifact ->
            artifact.toViewArtifact()
        }.sortedBy { it.name }

        return viewData
            .groupBy { it.name[0].uppercaseChar().toString() }
            .mapValues { it.value.toImmutableList() }
            .toImmutableMap()
    }
}

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failed<T>(val errorMessage: String) : State<T>()
}

data class OssViewState(
    val viewState: State<ImmutableMap<String, ImmutableList<ViewArtifact>>> = State.Loading(),
)

data class ViewArtifact(
    val name: String,
    val coordinate: String,
    val licenses: ImmutableList<ViewLicense>,
)

data class ViewLicense(
    val title: String,
    val identifier: String?,
    val url: String,
)

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
