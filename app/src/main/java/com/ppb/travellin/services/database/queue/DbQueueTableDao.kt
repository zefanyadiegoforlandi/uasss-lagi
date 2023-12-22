package com.ppb.travellin.services.database.queue

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DbQueueTableDao {

    @get:Query("SELECT * FROM queue_table ORDER BY id ASC")
    val selectAll: List<DbQueueTable>

    @get:Query("SELECT * FROM queue_table ORDER BY id DESC")
    val showQueue: List<DbQueueTable>

    @get:Query("SELECT COUNT(*) FROM queue_table")
    val count: Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(queueTable: DbQueueTable)

    @Query("DELETE FROM queue_table WHERE id = :id")
    fun deleteById(id: Int)

    @Query("DELETE FROM queue_table")
    fun deleteAll()
}