package jsondatabase.client

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


@Serializable
data class Request(
    val type: String,
    val key: String = "",
    val value: String = ""
) {
    override fun toString() = Json.encodeToString(this)

    companion object {
        fun build(args: List<String>): Request {
            val params = Array(3) { "" }
            val argNames = listOf("-t", "-k", "-v")

            args.chunked(2).forEach {
                params[argNames.indexOf(it.first())] = it.last()
            }

            return Request(params[0], params[1], params[2])
        }
    }
}