package jsondatabase.server

import jsondatabase.client.Request
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket

fun main() {
    var server = Server(12345)
    val db = JsonDatabase()
    while (true) {
        try {
            val request = Json.decodeFromString<Request>(server.receive())
            if (Operation.parse(request.type) == Operation.EXIT)
                return server.apply { send(Response("OK").toString()) }.close()
            server.send(db.execute(request).toString())
        } catch (e: Exception) {
            server.close()
            server = Server(12345, false)
        }
    }
}

class Server(port: Int, info: Boolean = true) {
    private val server = ServerSocket(port).also { if (info) println("Server started!") }
    private var socket = server.accept()
    private var input = DataInputStream(socket.getInputStream())
    private var output = DataOutputStream(socket.getOutputStream())

    fun receive(): String = input.readUTF().also { println("Received: $it") }
    fun send(response: String) = println("Sent: $response").run { output.writeUTF(response) }
    fun close() = server.close()
}

class JsonDatabase {

    private val database = mutableMapOf<String, String>()

    fun execute(request: Request): Response {
        try {
            when (Operation.parse(request.type)) {
                Operation.GET -> return Response(
                    response = "OK",
                    value = database[request.key]!!
                )

                Operation.SET -> database[request.key] = request.value
                Operation.DELETE -> if (database.containsKey(request.key)) {
                    database.remove(request.key)
                } else throw Exception()

                else -> throw Exception()
            }
            return Response("OK")
        } catch (_: Exception) {
            return Response(
                response = "ERROR",
                reason = "No such key"
            )
        }
    }
}

enum class Operation {
    GET, SET, DELETE, EXIT;

    companion object {
        fun parse(str: String) = Operation.valueOf(str.uppercase())
    }
}
