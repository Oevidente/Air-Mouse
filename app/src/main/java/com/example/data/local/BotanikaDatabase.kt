package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ShortcutEntity::class, ServerEntity::class], version = 1, exportSchema = false)
abstract class BotanikaDatabase : RoomDatabase() {
    abstract fun remoteDao(): RemoteDao

    companion object {
        @Volatile
        private var INSTANCE: BotanikaDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): BotanikaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BotanikaDatabase::class.java,
                    "botanika_remote.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
