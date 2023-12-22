package com.ppb.travellin.services.database.train_classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [TrainClassesTable::class], version = 1, exportSchema = true)
abstract class TrainClassesDatabase : RoomDatabase(){

    abstract fun trainClassesTableDao(): TrainClassesTableDao

    companion object {
        @Volatile
        private var INSTANCE: TrainClassesDatabase? = null

        fun getDatabase(context: Context): TrainClassesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    TrainClassesDatabase::class.java,
                    "train_classes_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }
}