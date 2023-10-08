package jsondatabase.client

import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.Socket

fun main(args: Array<String>) = Client("127.0.0.1", 12345).apply {
    send(Request.build(args.toList()).toString())
    receive()
}.close()

class Client(address: String, port: Int) {
    private val socket = Socket(address, port).also { println("Client started!") }
    private val input = DataInputStream(socket.getInputStream())
    private val output = DataOutputStream(socket.getOutputStream())

    fun send(request: String) = println("Sent: $request").also { output.writeUTF(request) }
    fun receive(): String = input.readUTF().also { println("Received: $it") }
    fun close() = socket.close()
}
