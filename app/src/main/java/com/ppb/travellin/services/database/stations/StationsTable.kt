package com.ppb.travellin.services.database.stations

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations_table")
data class StationsTable (

    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id: String = "",

    @ColumnInfo(name = "city")
    var city: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "weight")
    var weight: Int? = null,


)