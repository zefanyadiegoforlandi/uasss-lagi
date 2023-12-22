package com.ppb.travellin.services.database


interface GeneralDatabaseInterface<T> {

    val selectAll : List<T>

    fun search(keyword: String): List<T>

    fun getById(id: String): T

    fun insert(obj: T)

    fun update(obj: T)

    fun deleteById(id: String)

    fun deleteAll()

}