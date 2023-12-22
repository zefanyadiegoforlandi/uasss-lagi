package com.ppb.travellin.services.database.queue

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "queue_table")
data class DbQueueTable (

    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0,

    @ColumnInfo(name = "target_table")
    var targetTable: String? = null,

    @ColumnInfo(name = "target_action")
    var targetAction: String? = null,

    @ColumnInfo(name = "id_target")
    var idTarget: String? = null,

    @ColumnInfo(name = "name")
    var name: String? = null,

    @ColumnInfo(name = "weight")
    var weight: Int? = null,

    @ColumnInfo(name = "additional_data")
    var additionalData: String? = null,



)