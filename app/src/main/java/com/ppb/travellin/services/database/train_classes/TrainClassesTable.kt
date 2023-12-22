package com.ppb.travellin.services.database.train_classes

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "train_classes_table")
data class TrainClassesTable (

    @PrimaryKey(autoGenerate = false)
    @NonNull
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "weight")
    var weight: Int? = null,

)