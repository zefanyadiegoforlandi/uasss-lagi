package com.ppb.travellin.services.database.trains

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [TrainsTable::class], version = 1, exportSchema = true)
abstract class TrainsDatabase : RoomDatabase() {

    abstract fun trainsTableDao(): TrainsTableDao

    companion object {
        @Volatile
        private var INSTANCE: TrainsDatabase? = null

        fun getDatabase(context: Context): TrainsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    TrainsDatabase::class.java,
                    "trains_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}