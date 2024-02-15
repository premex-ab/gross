package se.premex.gross.core

import kotlinx.serialization.json.Json
import okio.BufferedSource

object LicenseParser {
    fun decode(source: BufferedSource): List<Artifact> {
        return Json.decodeFromString(source.readString(Charsets.UTF_8))
    }
}
