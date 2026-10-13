package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RemoteDao {
    @Query("SELECT * FROM shortcuts ORDER BY orderIndex ASC, id ASC")
    fun getAllShortcuts(): Flow<List<ShortcutEntity>>

    @Query("SELECT * FROM shortcuts ORDER BY orderIndex ASC, id ASC")
    suspend fun getAllShortcutsList(): List<ShortcutEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcut(shortcut: ShortcutEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShortcuts(shortcuts: List<ShortcutEntity>)

    @Update
    suspend fun updateShortcut(shortcut: ShortcutEntity)

    @Query("DELETE FROM shortcuts WHERE id = :id")
    suspend fun deleteShortcut(id: Long)

    @Query("DELETE FROM shortcuts WHERE id IN (:ids)")
    suspend fun deleteShortcutsByIds(ids: List<Long>)

    @Query("SELECT * FROM servers ORDER BY lastUsedAt DESC")
    fun getAllServers(): Flow<List<ServerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateServer(server: ServerEntity): Long

    @Query("DELETE FROM servers WHERE id = :id")
    suspend fun deleteServer(id: Long)

    @Query("SELECT COUNT(*) FROM shortcuts")
    suspend fun getShortcutCount(): Int
}
