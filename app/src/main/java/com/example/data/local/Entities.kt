package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.CustomShortcut
import com.example.data.model.RemoteServer

@Entity(tableName = "shortcuts")
data class ShortcutEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val keyCombo: String,
    val iconKey: String,
    val colorHex: String,
    val isSystemDefault: Boolean = false,
    val orderIndex: Int = 0
) {
    fun toDomain(): CustomShortcut = CustomShortcut(
        id = id,
        title = title,
        category = category,
        keyCombo = keyCombo,
        iconKey = iconKey,
        colorHex = colorHex,
        isSystemDefault = isSystemDefault
    )

    companion object {
        fun fromDomain(shortcut: CustomShortcut, orderIndex: Int = 0): ShortcutEntity =
            ShortcutEntity(
                id = shortcut.id,
                title = shortcut.title,
                category = shortcut.category,
                keyCombo = shortcut.keyCombo,
                iconKey = shortcut.iconKey,
                colorHex = shortcut.colorHex,
                isSystemDefault = shortcut.isSystemDefault,
                orderIndex = orderIndex
            )
    }
}

@Entity(tableName = "servers")
data class ServerEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val ipAddress: String,
    val udpPort: Int = 5005,
    val tcpPort: Int = 5006,
    val name: String,
    val lastUsedAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): RemoteServer = RemoteServer(
        id = id,
        ipAddress = ipAddress,
        udpPort = udpPort,
        tcpPort = tcpPort,
        name = name
    )

    companion object {
        fun fromDomain(server: RemoteServer): ServerEntity = ServerEntity(
            id = server.id,
            ipAddress = server.ipAddress,
            udpPort = server.udpPort,
            tcpPort = server.tcpPort,
            name = server.name,
            lastUsedAt = System.currentTimeMillis()
        )
    }
}
