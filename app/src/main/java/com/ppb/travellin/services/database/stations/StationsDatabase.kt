package com.ppb.travellin.services.database.stations

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StationsTable::class], version = 1, exportSchema = true)
abstract class StationsDatabase : RoomDatabase() {

    abstract fun stationsTableDao(): StationsTableDao

    companion object {
        @Volatile
        private var INSTANCE: StationsDatabase? = null

        fun getDatabase(context: Context): StationsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StationsDatabase::class.java,
                    "stations_database"
                )
                    // Tambahkan fallbackToDestructiveMigration jika skema berubah
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
