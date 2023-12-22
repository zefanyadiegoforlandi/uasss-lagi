package com.ppb.travellin.services.database.ticket_history

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TicketHistoryTableDao {

    @get:Query("SELECT * FROM ticket_history_table")
    val selectAll: List<TicketHistoryTable>?


    @Query("SELECT * FROM ticket_history_table WHERE tanggal_keberangkatan >= :currentTimestamp ORDER BY tanggal_keberangkatan ASC LIMIT 1")
    fun unComing(currentTimestamp: Long = System.currentTimeMillis()): TicketHistoryTable?

    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    fun insert(obj: TicketHistoryTable)

    @Query("DELETE FROM ticket_history_table")
    fun deleteAll()
}