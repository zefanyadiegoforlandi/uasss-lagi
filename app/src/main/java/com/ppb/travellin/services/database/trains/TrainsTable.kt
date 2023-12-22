package com.ppb.travellin.services.database.trains

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trains_table")
data class TrainsTable(

    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "weight")
    var weight: Int? = null,



)
