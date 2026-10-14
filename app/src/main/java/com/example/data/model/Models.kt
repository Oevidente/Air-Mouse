package com.example.data.model

enum class AirMouseMode {
    HOLD_TO_MOVE,
    TOGGLE
}

enum class OneHandedMode {
    OFF,        // Padrão / Ambas as Mãos
    RIGHT_HAND, // Destro (Otimizado para mão direita)
    LEFT_HAND   // Canhoto (Otimizado para mão esquerda)
}

enum class RemoteTab {
    AIR_MOUSE,
    MEDIA_CONTROLS,
    TOUCHPAD,
    SHORTCUTS_DECK,
    CONNECTION_SETTINGS
}

data class RemoteServer(
    val id: Long = 0,
    val ipAddress: String = "192.168.1.100",
    val udpPort: Int = 5005,
    val tcpPort: Int = 5006,
    val name: String = "Meu PC",
    val isConnected: Boolean = false,
    val pingMs: Long = -1L
)

data class CustomShortcut(
    val id: Long = 0,
    val title: String,
    val category: String,
    val keyCombo: String, // e.g. "F5", "CTRL+C", "ALT+TAB", "MEDIA_PLAY"
    val iconKey: String = "play",
    val colorHex: String = "#2979FF",
    val isSystemDefault: Boolean = false
)

data class UserPreferences(
    val gyroSensitivity: Float = 2.2f,
    val gyroDeadzone: Float = 0.04f,
    val gyroMode: AirMouseMode = AirMouseMode.TOGGLE,
    val invertX: Boolean = false,
    val invertY: Boolean = false,
    val touchpadSensitivity: Float = 1.6f,
    val hapticFeedbackEnabled: Boolean = true,
    val pcIp: String = "192.168.1.100",
    val pcUdpPort: Int = 5005,
    val pcTcpPort: Int = 5006
)
