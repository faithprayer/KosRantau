package com.example.login.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Kos::class],
    version = 1
)
abstract class KosDB: RoomDatabase() {
    abstract fun kosDao(): KosDAO

    companion object {
        @Volatile
        private var instance: KosDB? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                KosDB::class.java,
                "kos12345.db"
            ).build()
    }
}
