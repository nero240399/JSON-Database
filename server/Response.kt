package jsondatabase.server

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Response(
    val response: String,
    val reason: String = "",
    val value: String = ""
) {
    override fun toString(): String {
        return Json.encodeToString(this)
    }
}
