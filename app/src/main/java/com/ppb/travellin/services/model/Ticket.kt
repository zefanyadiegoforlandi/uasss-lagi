package com.ppb.travellin.services.model

data class Ticket(
    var ticket_id : Int? = 0,
    var user_id : String? = "",

    var tanggal_keberangkatan : String? = "",
    var nama_kereta : String? = "",
    var kelas_kereta : String? = "",
    var stasiun_asal : String? = "",
    var stasiun_tujuan : String? = "",


)
