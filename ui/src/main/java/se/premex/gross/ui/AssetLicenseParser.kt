package se.premex.gross.ui

import android.content.res.AssetManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import se.premex.gross.core.Artifact
import se.premex.gross.core.LicenseParser

class AssetLicenseParser(private val assetManager: AssetManager) {

    suspend fun readFromAssets(): List<Artifact> =
        withContext(Dispatchers.IO) {
            val source = assetManager.open("artifacts.json").source().buffer()
            LicenseParser.decode(source)
        }
}
