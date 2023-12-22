package com.ppb.travellin.services.database

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.Source
import com.ppb.travellin.TravellinApps
import com.ppb.travellin.services.api.FireConsole
import com.ppb.travellin.services.api.FireConsole.Companion.FIELD_ID
import com.ppb.travellin.services.database.queue.DbQueueTable
import com.ppb.travellin.services.database.stations.StationsTable
import com.ppb.travellin.services.database.train_classes.TrainClassesTable
import com.ppb.travellin.services.database.trains.TrainsTable
import com.ppb.travellin.services.model.Stations
import com.ppb.travellin.services.model.TrainClasses
import com.ppb.travellin.services.model.Trains
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseInformationManager(
    private val context: Context,
    private val app : Application,
    private val activity: AppCompatActivity
) : DatabaseInformationPrefManager(context) {

    private val factory = AppDatabaseViewModelFactory((app as TravellinApps).appRepository)
    private val appViewModel = ViewModelProvider(activity, factory)[AppDatabaseViewModel::class.java]

    private val fireConsole by lazy {
        FireConsole()
    }

    suspend fun checkAndUpdate() {
        withContext(Dispatchers.IO) {
            val vers = fireConsole.getInformationVersions()
            val stationVers = vers.stations
            val trainsVers = vers.trains
            val trainClassesVers = vers.trainClasses

            if (stationVers != null && !checkStationsDbVersion(stationVers)) {
                updateStationsDbVersion(stationVers)
                Log.i("DatabaseInformationManager", "Stations Updated")
            }
            if (trainsVers != null && !checkTrainsDbVersion(trainsVers)) {
                updateTrainsDbVersion(trainsVers)
                Log.i("DatabaseInformationManager", "Trains Updated")
            }
            if (trainClassesVers != null && !checkTrainClassesDbVersion(trainClassesVers)) {
                updateTrainClassesDbVersion(trainClassesVers)
                Log.i("DatabaseInformationManager", "Train Classes Updated")
            }

            Log.d("DatabaseInformationManager", "Check Update Completed")
        }
    }

    


    suspend fun sendQueue() {
        withContext(Dispatchers.IO) {
            val queue = appViewModel.listQueues()
            var isTrainUpdated = false
            var isStationUpdated = false
            var isTrainClassUpdated = false

            queue.forEach { q ->
                var table : Int? = null
                when(q.targetTable) {
                    "stations" -> {
                        table = 1
                        isStationUpdated = true
                    }
                    "trains" -> {
                        table = 2
                        isTrainUpdated = true
                    }
                    "train_classes" -> {
                        table = 3
                        isTrainClassUpdated = true
                    }
                }

                if (table != null) {
                    when(q.targetAction) {
                        "insert" -> insertToFireStore(q, table)
                        "update" -> updateToFireStore(q, table)
                        "delete" -> deleteToFireStore(q, table)
                        else ->
                            Log.e(
                                "DatabaseInformationManager",
                                "Error updating ${q.targetTable} : targetAction is not found"
                            )
                    }


                } else {
                    Log.e("DatabaseInformationManager", "Error updating ${q.targetTable} : tableRef is not found")
                }

            }

            if (isStationUpdated) {
                fireConsole.updateStationVersion()
            }
            if (isTrainUpdated) {
                fireConsole.updateTrainVersion()
            }
            if (isTrainClassUpdated) {
                fireConsole.updateTrainClassesVersion()
            }

            isStationUpdated = false
            isTrainUpdated = false
            isTrainClassUpdated = false
        }

    }





    /**
     * Delete To Firestore
     */

    private  fun deleteToFireStore(q: DbQueueTable, table: Int) {
        when(table) {
            1 -> {
                deleteStationToFireStore(q)
            }
            2 -> {
                deleteTrainToFireStore(q)
            }
            3 -> {
                deleteTrainClassToFireStore(q)
            }
            else ->
                Log.wtf("DatabaseInformationManager", "Error updating ${q.targetTable} : tableRef is not found. That Supposed to be found")
        }
    }

    private fun deleteTrainClassToFireStore(q: DbQueueTable) {
        fireConsole.trainsClassesRef.whereEqualTo(FIELD_ID, q.idTarget).limit(1).get(Source.SERVER).addOnSuccessListener {documents ->
            if (!documents.isEmpty) {
                fireConsole.trainsClassesRef.document(documents.documents[0].id).delete().addOnSuccessListener {
                    Log.d("DatabaseInformationManager", "Train Classes deleted")
                    appViewModel.deleteQueueById(q.id)
                }.addOnFailureListener {
                    Log.e("DatabaseInformationManager", "Error deleting train class id :", it)
                }
            } else {
                Log.e("DatabaseInformationManager", "Error deleting train class id : documents is empty")
            }
        }.addOnFailureListener { exception ->
            Log.e("DatabaseInformationManager", "Error deleting train class :", exception)
        }
    }

    private fun deleteTrainToFireStore(q: DbQueueTable) {
        fireConsole.trainsRef.whereEqualTo(FIELD_ID, q.idTarget).limit(1).get(Source.SERVER).addOnSuccessListener {documents ->
            if (!documents.isEmpty) {
                fireConsole.trainsRef.document(documents.documents[0].id).delete().addOnSuccessListener {
                    Log.d("DatabaseInformationManager", "Train deleted")
                    appViewModel.deleteQueueById(q.id)
                }.addOnFailureListener {
                    Log.e("DatabaseInformationManager", "Error deleting train id :", it)
                }
            } else {
                Log.e("DatabaseInformationManager", "Error deleting train id : documents is empty")
            }
        }.addOnFailureListener { exception ->
            Log.e("DatabaseInformationManager", "Error deleting train :", exception)
        }
    }

    private fun deleteStationToFireStore(q: DbQueueTable) {
        fireConsole.stationsRef.whereEqualTo(FIELD_ID, q.idTarget).limit(1).get(Source.SERVER).addOnSuccessListener {documents ->
            if (!documents.isEmpty) {
                fireConsole.stationsRef.document(documents.documents[0].id).delete().addOnSuccessListener {
                    Log.d("DatabaseInformationManager", "Station deleted")
                    appViewModel.deleteQueueById(q.id)
                }.addOnFailureListener {
                    Log.e("DatabaseInformationManager", "Error deleting station id :", it)
                }
            } else {
                Log.e("DatabaseInformationManager", "Error deleting station id : documents is empty")
            }
        }.addOnFailureListener { exception ->
            Log.e("DatabaseInformationManager", "Error deleting station :", exception)
        }
    }


    /**
     * Update To Firestore
     */

    private fun updateToFireStore(q: DbQueueTable, table: Int) {
        when(table) {
            1 -> {
                Log.d("DatabaseInformationManager", "Update Station")
                deleteStationToFireStore(q)
                insertStationToFireStore(q)
            }
            2 -> {
                Log.d("DatabaseInformationManager", "Update Train")
                deleteTrainToFireStore(q)
                insertTrainToFireStore(q)
            }
            3 -> {
                Log.d("DatabaseInformationManager", "Update Train Class")
                deleteTrainClassToFireStore(q)
                insertTrainClassToFireStore(q)
            }
            else ->
                Log.wtf("DatabaseInformationManager", "Error updating ${q.targetTable} : tableRef is not found. That Supposed to be found")
        }
    }



    /**
     * Insert To Firestore
     */

    private fun insertToFireStore(q: DbQueueTable, tableSelected: Int) {
        when(tableSelected) {
            1 -> {
                insertStationToFireStore(q)
            }
            2 -> {
                insertTrainToFireStore(q)
            }
            3 -> {
                insertTrainClassToFireStore(q)
            }
            else ->
                Log.wtf("DatabaseInformationManager", "Error updating ${q.targetTable} : tableRef is not found. That Supposed to be found")
        }
    }

    private fun insertTrainClassToFireStore(q: DbQueueTable) {
        val trainClass = TrainClasses(
            name = q.name,
            weight = q.weight
        )


        fireConsole.trainsClassesRef.add(trainClass).addOnSuccessListener { documentReference ->
            trainClass.id = documentReference.id
            documentReference.set(trainClass).addOnSuccessListener {
                Log.d("DatabaseInformationManager", "TrainClasses added")
                appViewModel.deleteQueueById(q.id)
            }.addOnFailureListener {
                Log.e("DatabaseInformationManager", "Error adding train class id :", it)
            }
        }.addOnFailureListener {
            Log.e("DatabaseInformationManager", "Error adding train class :", it)
        }
    }

    private fun insertTrainToFireStore(q: DbQueueTable) {
        val train = Trains(
            name = q.name,
            weight = q.weight
        )


        fireConsole.trainsRef.add(train).addOnSuccessListener { documentReference ->
            train.id = documentReference.id
            documentReference.set(train).addOnSuccessListener {
                Log.d("DatabaseInformationManager", "Train added")
                appViewModel.deleteQueueById(q.id)
            }.addOnFailureListener {
                Log.e("DatabaseInformationManager", "Error adding train id :", it)
            }
        }.addOnFailureListener {
            Log.e("DatabaseInformationManager", "Error adding train :", it)
        }
    }

    private fun insertStationToFireStore(q: DbQueueTable) {
        val station = Stations(
            name = q.name,
            city = q.additionalData,
            weight = q.weight
        )


        fireConsole.stationsRef.add(station).addOnSuccessListener { documentReference ->
            station.id = documentReference.id
            documentReference.set(station).addOnSuccessListener {
                Log.d("DatabaseInformationManager", "Station added")
                appViewModel.deleteQueueById(q.id)
            }.addOnFailureListener {
                Log.e("DatabaseInformationManager", "Error adding station id :", it)
            }
        }.addOnFailureListener {
            Log.e("DatabaseInformationManager", "Error adding station :", it)
        }
    }


    /**
     * Local Database
     */
    private fun updateTrainClassesDbVersion(trainClassesVers: String) {
        saveTrainClassesDbVersion(trainClassesVers)
        Log.d("DatabaseInformationManager", "Train Classes get Version $trainClassesVers")
        fireConsole.trainsClassesRef.get(Source.SERVER).addOnSuccessListener {documents ->
            val trainClasses = documents.toObjects(TrainClasses::class.java)
            trainClasses.forEach { trainClass ->
                appViewModel.insertTrainClass(
                    TrainClassesTable(
                    id = trainClass.id!!,
                    name = trainClass.name,
                    weight = trainClass.weight,
                    )
                )
            }
        }.addOnFailureListener { exception ->
            Log.e("DatabaseInformationManager", "Error updating train classes :", exception) }
    }

    private fun updateTrainsDbVersion(trainsVers: String) {
        saveTrainsDbVersion(trainsVers)
        Log.d("DatabaseInformationManager", "Trains get Version $trainsVers")
        fireConsole.trainsRef.get(Source.SERVER).addOnSuccessListener {documents ->
            val trains = documents.toObjects(Trains::class.java)
            trains.forEach { train ->
                appViewModel.insertTrain(
                    TrainsTable(
                    id = train.id!!,
                    name = train.name,
                    weight = train.weight,
                    )
                )
            }
        }.addOnFailureListener { exception ->
            Log.e("DatabaseInformationManager", "Error updating trains :", exception) }

    }

    private fun updateStationsDbVersion(version: String) {
        saveStationsDbVersion(version)
        appViewModel.deleteAllStations()
        Log.d("DatabaseInformationManager", "Stations get Version $version")
        fireConsole.stationsRef.get(Source.SERVER).addOnSuccessListener {documents ->
            val stations = documents.toObjects(Stations::class.java)
            stations.forEach { station ->
                appViewModel.insertStation(StationsTable(
                    id = station.id!!,
                    name = station.name,
                    city = station.city,
                    weight = station.weight,
                    )
                )
            }
        }.addOnFailureListener { exception ->
            Log.e("DatabaseInformationManager", "Error updating stations :", exception) }
    }

}