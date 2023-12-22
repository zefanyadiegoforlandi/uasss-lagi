package com.ppb.travellin.services.database

import com.ppb.travellin.services.model.PaketModel

object PaketDatabase {

    private val _paketList = listOf<PaketModel>(
        PaketModel(
            id = 1,
            name = "Paket 1",
            description = "Pemandangan Panorama",
            price = 15000,
            status = false
        ),
        PaketModel(
            id = 2,
            name = "Paket 2",
            description = "Minuman Signature",
            price = 200000,
            status = false
        ),
        PaketModel(
            id = 3,
            name = "Paket 3",
            description = "Makanan Berat",
            price = 50000,
            status = false
        ),
        PaketModel(
            id = 4,
            name = "Paket 4",
            description = "Akses Hiburan Film",
            price = 40000,
            status = false
        ),
        PaketModel(
            id = 5,
            name = "Paket 5",
            description = "Makanan Ringan",
            price = 25000,
            status = false
        ),
        PaketModel(
            id = 6,
            name = "Paket 6",
            description = "Jaminan Duduk Dekat jendela",
            price = 15000,
            status = false
        ),
        PaketModel(
            id = 7,
            name = "Paket 7",
            description = "Berkebutuhan Khusus",
            price = 0,
            status = false
        ),
    )

    val paketList : List<PaketModel>
        get() = _paketList.toList().onEach { it.status = false }

}