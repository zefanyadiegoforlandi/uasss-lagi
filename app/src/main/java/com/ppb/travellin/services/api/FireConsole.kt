package com.ppb.travellin.services.api

import android.util.Log
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class FireConsole {
    internal val firebase = Firebase.firestore

    private val _usersCollectionRef = firebase.collection("users")
    val usersRef
        get() = _usersCollectionRef


    private val _information = firebase.collection("informations").document("KvUbgNvAq9n4jzB4Q1wx")

    private val _stations = _information.collection("stations")
    val stationsRef
        get() = _stations

    private val _trains = _information.collection("trains")
    val trainsRef
        get() = _trains

    private val _trainClasses = _information.collection("train_classes")
    val trainsClassesRef
        get() = _trainClasses

    fun updateTrainVersion() {
        _information
            .update(FIELD_VERSION_TRAINS, generateRandomId())
            .addOnSuccessListener {
                Log.d("FireConsole", "Train version updated")
            }
            .addOnFailureListener { exception ->
                Log.e("FireConsole", "Error updating train version :", exception)
            }
    }

    fun updateStationVersion() {
        _information
            .update(FIELD_VERSION_STATIONS, generateRandomId())
            .addOnSuccessListener {
                Log.d("FireConsole", "Station version updated")
            }
            .addOnFailureListener { exception ->
                Log.e("FireConsole", "Error updating station version :", exception)
            }
    }

    fun updateTrainClassesVersion() {
        _information
            .update(FIELD_VERSION_TRAIN_CLASSES, generateRandomId())
            .addOnSuccessListener {
                Log.d("FireConsole", "Train classes version updated")
            }
            .addOnFailureListener { exception ->
                Log.e("FireConsole", "Error updating train classes version :", exception)
            }
    }

    private fun generateRandomId(): String {
        val allowedChars = ('1'..'9') + ('A'..'Z') + ('a'..'z')
        return (1..20)
            .map { allowedChars.random() }
            .joinToString("")
    }


    // get train version
    suspend fun getInformationVersions() : InformationVersions = suspendCoroutine { continuation ->
        val vers = InformationVersions(null, null, null)

        _information.get(Source.SERVER).addOnSuccessListener { document ->
            if (document != null) {
                vers.trains = document.getString(FIELD_VERSION_TRAINS)
                vers.stations = document.getString(FIELD_VERSION_STATIONS)
                vers.trainClasses = document.getString(FIELD_VERSION_TRAIN_CLASSES)
            } else {
                Log.d("FireConsole", "No such document")
            }
            continuation.resume(vers)
        }.addOnFailureListener { exception ->
            Log.d("FireConsole", "get failed with ", exception)
            continuation.resume(vers)
        }
    }


    data class InformationVersions(
        var stations: String?,
        var trains: String?,
        var trainClasses: String?
    )

    companion object {
        const val FIELD_ID = "id"
        const val FIELD_NAME = "name"
        const val FIELD_WEIGHT = "weight"
        const val FIELD_CITY = "city"

        const val FIELD_VERSION_STATIONS = "version_stations"
        const val FIELD_VERSION_TRAINS = "version_trains"
        const val FIELD_VERSION_TRAIN_CLASSES = "version_train_classes"
    }
}