package com.ppb.travellin.services.model

class PaketModel(
    val id: Int,
    val name: String,
    val description: String,
    val price: Int,
    var status: Boolean = false,
) {
}