package com.ppb.travellin.services.database.train_classes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ppb.travellin.services.database.GeneralDatabaseInterface

@Dao
interface TrainClassesTableDao : GeneralDatabaseInterface<TrainClassesTable>{

    @get:Query("SELECT * FROM train_classes_table")
    override val selectAll: List<TrainClassesTable>


    @Query("SELECT * FROM train_classes_table WHERE name LIKE :keyword ORDER BY name ASC")
    override fun search(keyword: String): List<TrainClassesTable>


    @Query("SELECT * FROM train_classes_table WHERE id = :id LIMIT 1")
    override fun getById(id: String): TrainClassesTable


    @Insert(onConflict = androidx.room.OnConflictStrategy.IGNORE)
    override fun insert(obj: TrainClassesTable)


    @Update
    override fun update(obj: TrainClassesTable)


    @Query("DELETE FROM train_classes_table WHERE id = :id")
    override fun deleteById(id: String)


    @Query("DELETE FROM train_classes_table")
    override fun deleteAll()
}