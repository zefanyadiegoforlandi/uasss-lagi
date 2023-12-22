package com.ppb.travellin.services.database.ticket_history

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ticket_history_table")
data class TicketHistoryTable(

    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int = 0,

    @ColumnInfo(name = "tanggal_keberangkatan")
    var tanggalKeberangkatan: Date? = null,

    @ColumnInfo(name = "kereta")
    var kereta: String? = null,

    @ColumnInfo(name = "kelas")
    var kelas: String? = null,

    @ColumnInfo(name = "harga")
    var harga: Int? = null,

    @ColumnInfo(name = "kota_asal")
    var kotaAsal: String? = null,

    @ColumnInfo(name = "stasiun_asal")
    var stasiunAsal: String? = null,

    @ColumnInfo(name = "kota_tujuan")
    var kotaTujuan: String? = null,

    @ColumnInfo(name = "stasiun_tujuan")
    var stasiunTujuan: String? = null,

    @ColumnInfo(name = "paket")
    var paketBinary: String = "0000000",


    )
