package com.ppb.travellin.services.database.ticket_history

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ppb.travellin.formatter.DateConverter

@Database(entities = [TicketHistoryTable::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
abstract class TicketHistoryDatabase : RoomDatabase() {

    abstract fun ticketHistoryTableDao(): TicketHistoryTableDao

    companion object {
        @Volatile
        private var INSTANCE: TicketHistoryDatabase? = null

        fun getDatabase(context: Context): TicketHistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    TicketHistoryDatabase::class.java,
                    "ticket_history_database"
                ).build()
                INSTANCE = instance
                instance
            }

        }
    }

}