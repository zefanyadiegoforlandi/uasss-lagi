package com.ppb.travellin.services.database.stations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ppb.travellin.services.database.GeneralDatabaseInterface

@Dao
interface StationsTableDao : GeneralDatabaseInterface<StationsTable> {

    @get:Query("SELECT * FROM stations_table ORDER BY city ASC")
    override val selectAll: List<StationsTable>

    @Query("SELECT * FROM stations_table WHERE city LIKE :keyword OR name LIKE :keyword ORDER BY city ASC")
    override fun search(keyword: String): List<StationsTable>

    @Query("SELECT * FROM stations_table WHERE id = :id LIMIT 1")
    override fun getById(id: String): StationsTable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override fun insert(obj: StationsTable)

    @Update
    override fun update(obj: StationsTable)

    @Query("DELETE FROM stations_table WHERE id = :id")
    override fun deleteById(id: String)


    @Query("DELETE FROM stations_table")
    override fun deleteAll()
}