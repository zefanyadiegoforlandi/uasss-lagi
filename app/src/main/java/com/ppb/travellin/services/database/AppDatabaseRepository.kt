package com.ppb.travellin.services.database

import android.util.Log
import com.ppb.travellin.services.database.queue.DbQueueTable
import com.ppb.travellin.services.database.queue.DbQueueTableDao
import com.ppb.travellin.services.database.stations.StationsTable
import com.ppb.travellin.services.database.stations.StationsTableDao
import com.ppb.travellin.services.database.ticket_history.TicketHistoryTable
import com.ppb.travellin.services.database.ticket_history.TicketHistoryTableDao
import com.ppb.travellin.services.database.train_classes.TrainClassesTable
import com.ppb.travellin.services.database.train_classes.TrainClassesTableDao
import com.ppb.travellin.services.database.trains.TrainsTable
import com.ppb.travellin.services.database.trains.TrainsTableDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppDatabaseRepository(
    private val trainsTableDao : TrainsTableDao,
    private val stationsTableDao : StationsTableDao,
    private val trainClassesTableDao : TrainClassesTableDao,
    private val queueTableDao: DbQueueTableDao,
    private val ticketHistoryTableDao: TicketHistoryTableDao
) {

    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    // Train
    suspend fun listTrains () : List<TrainsTable> {
        return withContext(Dispatchers.IO) { trainsTableDao.selectAll }
    }

    suspend fun searchTrains(keyword: String): List<TrainsTable> {
        Log.d("AppDatabaseRepository", "searchTrains: $keyword")
        return withContext(Dispatchers.IO) { trainsTableDao.search("%$keyword%") }
    }

    suspend fun getTrainById(id: String): TrainsTable {
        Log.d("AppDatabaseRepository", "getTrainById: $id")
        return withContext(Dispatchers.IO) { trainsTableDao.getById(id) }
    }

    fun insertTrain(train: TrainsTable) {
        Log.i("AppDatabaseRepository", "insertTrain: $train")
        executorService.execute {
            trainsTableDao.insert(train)
        }
    }

    fun updateTrain(train: TrainsTable) {
        Log.i("AppDatabaseRepository", "updateTrain: $train")
        executorService.execute {
            trainsTableDao.update(train)
        }
    }

    fun deleteTrainById(id: String) {
        Log.i("AppDatabaseRepository", "deleteTrainById: $id")
        executorService.execute {
            trainsTableDao.deleteById(id)
        }
    }

    fun deleteAllTrains() {
        Log.i("AppDatabaseRepository", "deleteAllTrains")
        executorService.execute {
            trainsTableDao.deleteAll()
        }
    }


    // Station
    suspend fun listStations () : List<StationsTable> {
        return withContext(Dispatchers.IO) { stationsTableDao.selectAll }
    }

    suspend fun searchStations(keyword: String): List<StationsTable> {
        Log.d("AppDatabaseRepository", "searchStations: $keyword")
        return withContext(Dispatchers.IO) { stationsTableDao.search("%$keyword%") }
    }

    suspend fun getStationById(id: String): StationsTable {
        Log.d("AppDatabaseRepository", "getStationById: $id")
        return withContext(Dispatchers.IO) { stationsTableDao.getById(id) }
    }

    fun insertStation(station: StationsTable) {
        Log.i("AppDatabaseRepository", "insertStation: $station")
        executorService.execute {
            stationsTableDao.insert(station)
        }
    }

    fun updateStation(station: StationsTable) {
        Log.i("AppDatabaseRepository", "updateStation: $station")
        executorService.execute {
            stationsTableDao.update(station)
        }
    }

    fun deleteStationById(id: String) {
        Log.i("AppDatabaseRepository", "deleteStationById: $id")
        executorService.execute {
            stationsTableDao.deleteById(id)
        }
    }

    fun deleteAllStations() {
        Log.i("AppDatabaseRepository", "deleteAllStations")
        executorService.execute {
            stationsTableDao.deleteAll()
        }
    }


    // Train Class

    suspend fun listTrainClasses () : List<TrainClassesTable> {
        return withContext(Dispatchers.IO) { trainClassesTableDao.selectAll }
    }


    suspend fun searchTrainClasses(keyword: String): List<TrainClassesTable> {
        Log.d("AppDatabaseRepository", "searchTrainClasses: $keyword")
        return withContext(Dispatchers.IO) { trainClassesTableDao.search("%$keyword%") }
    }

    suspend fun getTrainClassById(id: String): TrainClassesTable {
        Log.d("AppDatabaseRepository", "getTrainClassById: $id")
        return withContext(Dispatchers.IO) { trainClassesTableDao.getById(id) }
    }

    fun insertTrainClass(trainClass: TrainClassesTable) {
        Log.i("AppDatabaseRepository", "insertTrainClass: $trainClass")
        executorService.execute {
            trainClassesTableDao.insert(trainClass)
        }
    }

    fun updateTrainClass(trainClass: TrainClassesTable) {
        Log.i("AppDatabaseRepository", "updateTrainClass: $trainClass")
        executorService.execute {
            trainClassesTableDao.update(trainClass)
        }
    }

    fun deleteTrainClassById(id: String) {
        Log.i("AppDatabaseRepository", "deleteStationById: $id")
        executorService.execute {
            trainClassesTableDao.deleteById(id)
        }
    }

    fun deleteAllTrainClasses() {
        Log.i("AppDatabaseRepository", "deleteAllStations")
        executorService.execute {
            trainClassesTableDao.deleteAll()
        }
    }


    // Queue

    suspend fun listQueue () : List<DbQueueTable> {
        return withContext(Dispatchers.IO) { queueTableDao.selectAll }
    }
    suspend fun showListQueue () : List<DbQueueTable> {
        return withContext(Dispatchers.IO) { queueTableDao.showQueue }
    }
    suspend fun countQueue () : Int {
        return withContext(Dispatchers.IO) { queueTableDao.count }
    }

    fun insertQueue(queue: DbQueueTable) {
        Log.i("AppDatabaseRepository", "insertQueue: $queue")
        executorService.execute {
            queueTableDao.insert(queue)
        }
    }

    fun deleteQueueById(id: Int) {
        Log.i("AppDatabaseRepository", "deleteQueueById: $id")
        executorService.execute {
            queueTableDao.deleteById(id)
        }
    }

    fun deleteAllQueue() {
        Log.i("AppDatabaseRepository", "deleteAllQueue")
        executorService.execute {
            queueTableDao.deleteAll()
        }
    }



    // Ticket History
    suspend fun listTicketHistory () : List<TicketHistoryTable>? {
        return withContext(Dispatchers.IO) {ticketHistoryTableDao.selectAll }
    }
    suspend fun upComingTicketHistory () : TicketHistoryTable? {
        return withContext(Dispatchers.IO) { ticketHistoryTableDao.unComing() }
    }

    fun insertTicketHistory(ticketHistory: TicketHistoryTable) {
        Log.i("AppDatabaseRepository", "insertTicketHistory: $ticketHistory")
        executorService.execute {
            ticketHistoryTableDao.insert(ticketHistory)
        }
    }

    fun deleteAllTicketHistory() {
        Log.i("AppDatabaseRepository", "deleteAllTicketHistory")
        executorService.execute {
            ticketHistoryTableDao.deleteAll()
        }
    }



}