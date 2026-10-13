package com.example.data.repository

import com.example.data.local.DefaultShortcuts
import com.example.data.local.RemoteDao
import com.example.data.local.ServerEntity
import com.example.data.local.ShortcutEntity
import com.example.data.model.CustomShortcut
import com.example.data.model.RemoteServer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RemoteRepository(private val remoteDao: RemoteDao) {

    val shortcuts: Flow<List<CustomShortcut>> = remoteDao.getAllShortcuts().map { list ->
        list.map { it.toDomain() }
    }

    val servers: Flow<List<RemoteServer>> = remoteDao.getAllServers().map { list ->
        list.map { it.toDomain() }
    }

    suspend fun saveShortcut(shortcut: CustomShortcut) {
        remoteDao.insertShortcut(ShortcutEntity.fromDomain(shortcut))
    }

    suspend fun deleteShortcut(id: Long) {
        remoteDao.deleteShortcut(id)
    }

    suspend fun saveServer(server: RemoteServer) {
        remoteDao.insertOrUpdateServer(ServerEntity.fromDomain(server))
    }

    suspend fun deleteServer(id: Long) {
        remoteDao.deleteServer(id)
    }

    suspend fun ensureDefaultShortcuts() {
        val allShortcuts = remoteDao.getAllShortcutsList()
        if (allShortcuts.isEmpty()) {
            remoteDao.insertShortcuts(DefaultShortcuts.getInitialShortcuts())
            return
        }

        // Deduplicação retroativa: identificar e eliminar entradas redundantes
        val seenSignatures = mutableSetOf<String>()
        val duplicatesToDelete = mutableListOf<Long>()

        for (shortcut in allShortcuts) {
            val signature = "${shortcut.title.trim().lowercase()}|${shortcut.keyCombo.trim().lowercase()}"
            if (seenSignatures.contains(signature)) {
                duplicatesToDelete.add(shortcut.id)
            } else {
                seenSignatures.add(signature)
            }
        }

        if (duplicatesToDelete.isNotEmpty()) {
            remoteDao.deleteShortcutsByIds(duplicatesToDelete)
        }
    }
}
