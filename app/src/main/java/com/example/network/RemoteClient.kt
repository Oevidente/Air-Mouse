package com.example.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress
import java.net.Socket
import java.util.Locale

class RemoteClient(private val scope: CoroutineScope) {

    private val tag = "RemoteClient"
    private var udpSocket: DatagramSocket? = null
    private var cachedServerAddress: InetAddress? = null
    private var currentIp: String = ""
    private var currentUdpPort: Int = 5005
    private var currentTcpPort: Int = 5006

    init {
        try {
            udpSocket = DatagramSocket()
        } catch (e: Exception) {
            Log.e(tag, "Falha ao inicializar DatagramSocket", e)
        }
    }

    fun updateTarget(ip: String, udpPort: Int, tcpPort: Int) {
        if (currentIp != ip || currentUdpPort != udpPort || currentTcpPort != tcpPort) {
            currentIp = ip
            currentUdpPort = udpPort
            currentTcpPort = tcpPort
            scope.launch(Dispatchers.IO) {
                try {
                    cachedServerAddress = InetAddress.getByName(ip)
                } catch (e: Exception) {
                    cachedServerAddress = null
                }
            }
        }
    }

    fun sendMouseMove(dx: Float, dy: Float) {
        val target = cachedServerAddress ?: return
        val socket = udpSocket ?: return
        if (dx == 0f && dy == 0f) return

        // Compact JSON format for UDP delivery with sub-10ms latency
        val json = String.format(Locale.US, "{\"t\":\"move\",\"dx\":%.2f,\"dy\":%.2f}", dx, dy)
        sendUdpPayload(socket, target, currentUdpPort, json)
    }

    fun sendClick(button: String = "left") {
        val target = cachedServerAddress ?: return
        val socket = udpSocket ?: return
        val json = "{\"t\":\"click\",\"btn\":\"$button\"}"
        sendUdpPayload(socket, target, currentUdpPort, json)
    }

    fun sendMouseDown(button: String = "left") {
        val target = cachedServerAddress ?: return
        val socket = udpSocket ?: return
        val json = "{\"t\":\"m_down\",\"btn\":\"$button\"}"
        sendUdpPayload(socket, target, currentUdpPort, json)
    }

    fun sendMouseUp(button: String = "left") {
        val target = cachedServerAddress ?: return
        val socket = udpSocket ?: return
        val json = "{\"t\":\"m_up\",\"btn\":\"$button\"}"
        sendUdpPayload(socket, target, currentUdpPort, json)
    }

    fun sendScroll(dy: Int, dx: Int = 0) {
        val target = cachedServerAddress ?: return
        val socket = udpSocket ?: return
        val json = "{\"t\":\"scroll\",\"dy\":$dy,\"dx\":$dx}"
        sendUdpPayload(socket, target, currentUdpPort, json)
    }

    private fun sendUdpPayload(socket: DatagramSocket, address: InetAddress, port: Int, payload: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val bytes = payload.toByteArray(Charsets.UTF_8)
                val packet = DatagramPacket(bytes, bytes.size, address, port)
                socket.send(packet)
            } catch (e: Exception) {
                // Silently drop in UDP to maintain maximum real-time throughput
            }
        }
    }

    fun sendKey(combo: String) {
        val ip = currentIp
        val port = currentTcpPort
        if (ip.isBlank()) return

        scope.launch(Dispatchers.IO) {
            sendTcpCommand(ip, port, "{\"t\":\"key\",\"combo\":\"$combo\"}")
        }
    }

    fun sendText(text: String) {
        val ip = currentIp
        val port = currentTcpPort
        if (ip.isBlank()) return

        scope.launch(Dispatchers.IO) {
            val escaped = text.replace("\"", "\\\"")
            sendTcpCommand(ip, port, "{\"t\":\"type\",\"text\":\"$escaped\"}")
        }
    }

    private fun sendTcpCommand(ip: String, port: Int, payload: String) {
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(ip, port), 800)
                socket.soTimeout = 1000
                val out = PrintWriter(socket.getOutputStream(), true)
                out.println(payload)
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                reader.readLine() // aguarda ack
            }
        } catch (e: Exception) {
            Log.w(tag, "Falha no envio TCP de comando para $ip:$port - ${e.message}")
        }
    }

    suspend fun ping(ip: String, port: Int, timeoutMs: Int = 1200): Long = withContext(Dispatchers.IO) {
        val start = System.currentTimeMillis()
        try {
            Socket().use { socket ->
                socket.connect(InetSocketAddress(ip, port), timeoutMs)
                socket.soTimeout = timeoutMs
                val out = PrintWriter(socket.getOutputStream(), true)
                out.println("{\"t\":\"ping\"}")
                val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
                val response = reader.readLine()
                if (response != null && response.contains("pong")) {
                    return@withContext System.currentTimeMillis() - start
                }
            }
            -1L
        } catch (e: Exception) {
            -1L
        }
    }

    fun close() {
        try {
            udpSocket?.close()
        } catch (e: Exception) {
            // ignore
        }
    }
}
