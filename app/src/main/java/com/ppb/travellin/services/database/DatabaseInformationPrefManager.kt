package com.ppb.travellin.services.database

import android.content.Context


/**
 * Curent Database Information Manager
 * @property stationsDbVersion String
 * @property trainsDbVersion String
 * @property trainClassesDbVersion String
 *
 */
open class DatabaseInformationPrefManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("database_information", Context.MODE_PRIVATE)
    private val SPSV : String = "stations_version"
    private val SPTV : String = "trains_version"
    private val SPSTCV : String = "train_classes_version"



    // Stations Version

    val stationsDbVersion : String?
        get() = sharedPreferences.getString(SPSV, null)

    fun saveStationsDbVersion(version: String) {
        with(sharedPreferences.edit()) {
            putString(SPSV, version)
            apply()
        }
    }

    fun checkStationsDbVersion(version: String) : Boolean {
        return stationsDbVersion == version
    }



    // Trains Version

    val trainsDbVersion : String?
        get() = sharedPreferences.getString(SPTV, null)

    fun saveTrainsDbVersion(version: String) {
        with(sharedPreferences.edit()) {
            putString(SPTV, version)
            apply()
        }
    }

    fun checkTrainsDbVersion(version: String) : Boolean {
        return trainsDbVersion == version
    }



    // Train Classes Version

    val trainClassesDbVersion : String?
        get() = sharedPreferences.getString(SPSTCV, null)

    fun saveTrainClassesDbVersion(version: String) {
        with(sharedPreferences.edit()) {
            putString(SPSTCV, version)
            apply()
        }
    }

    fun checkTrainClassesDbVersion(version: String) : Boolean {
        return trainClassesDbVersion == version
    }

}