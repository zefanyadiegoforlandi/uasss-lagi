package com.ppb.travellin.services.database.queue

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [DbQueueTable::class], version = 1, exportSchema = true)
abstract class DbQueueDatabase : RoomDatabase() {

    abstract fun dbQueueTableDao(): DbQueueTableDao

    companion object {
        @Volatile
        private var INSTANCE: DbQueueDatabase? = null

        fun getDatabase(context: Context): DbQueueDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    DbQueueDatabase::class.java,
                    "db_queue_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }

}