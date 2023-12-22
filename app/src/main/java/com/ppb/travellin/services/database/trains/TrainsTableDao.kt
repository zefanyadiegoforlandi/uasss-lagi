package com.ppb.travellin.services.database.trains

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ppb.travellin.services.database.GeneralDatabaseInterface

@Dao
interface TrainsTableDao : GeneralDatabaseInterface<TrainsTable> {

    @get:Query("SELECT * FROM trains_table")
    override val selectAll: List<TrainsTable>

    @Query("SELECT * FROM trains_table WHERE name LIKE :keyword ORDER BY name ASC")
    override fun search(keyword: String): List<TrainsTable>

    @Query("SELECT * FROM trains_table WHERE id = :id LIMIT 1")
    override fun getById(id: String): TrainsTable

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    override fun insert(obj: TrainsTable)

    @Update
    override fun update(obj: TrainsTable)

    @Query("DELETE FROM trains_table WHERE id = :id")
    override fun deleteById(id: String)

    @Query("DELETE FROM trains_table")
    override fun deleteAll()

}