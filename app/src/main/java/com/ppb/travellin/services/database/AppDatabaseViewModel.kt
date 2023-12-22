package com.ppb.travellin.services.database

import androidx.lifecycle.ViewModel
import com.ppb.travellin.services.database.queue.DbQueueTable
import com.ppb.travellin.services.database.stations.StationsTable
import com.ppb.travellin.services.database.ticket_history.TicketHistoryTable
import com.ppb.travellin.services.database.train_classes.TrainClassesTable
import com.ppb.travellin.services.database.trains.TrainsTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Date

class AppDatabaseViewModel(
    private val appDatabaseRepository: AppDatabaseRepository
) : ViewModel() {

    // Train
    suspend fun listTrains () = withContext(Dispatchers.IO) {appDatabaseRepository.listTrains()}

    suspend fun searchTrains(keyword: String) : List<TrainsTable>{
        return appDatabaseRepository.searchTrains(keyword)
    }

    suspend fun getTrainById(id: String) : TrainsTable{
        return appDatabaseRepository.getTrainById(id)
    }

    fun insertTrain(train: TrainsTable) {
        appDatabaseRepository.insertTrain(train)
    }

    fun updateTrain(train: TrainsTable) {
        appDatabaseRepository.updateTrain(train)
    }

    fun deleteTrainById(id: String) {
        appDatabaseRepository.deleteTrainById(id)
    }

    fun deleteAllTrains() {
        appDatabaseRepository.deleteAllTrains()
    }


    // Station

    suspend fun listStations () = appDatabaseRepository.listStations()

    suspend fun searchStations(keyword: String) : List<StationsTable>{
        return appDatabaseRepository.searchStations(keyword)
    }

    suspend fun getStationById(id: String) : StationsTable{
        return appDatabaseRepository.getStationById(id)
    }

    fun insertStation(station: StationsTable) {
        appDatabaseRepository.insertStation(station)
    }

    fun updateStation(station: StationsTable) {
        appDatabaseRepository.updateStation(station)
    }

    fun deleteStationById(id: String) {
        appDatabaseRepository.deleteStationById(id)
    }

    fun deleteAllStations() {
        appDatabaseRepository.deleteAllStations()
    }




    // Train Classes

    suspend fun listTrainClasses () = appDatabaseRepository.listTrainClasses()

    suspend fun searchTrainClasses(keyword: String) : List<TrainClassesTable>{
        return appDatabaseRepository.searchTrainClasses(keyword)
    }

    suspend fun getTrainClassById(id: String) : TrainClassesTable{
        return appDatabaseRepository.getTrainClassById(id)
    }

    fun insertTrainClass(trainClass: TrainClassesTable) {
        appDatabaseRepository.insertTrainClass(trainClass)
    }

    fun updateTrainClass(trainClass: TrainClassesTable) {
        appDatabaseRepository.updateTrainClass(trainClass)
    }

    fun deleteTrainClassById(id: String) {
        appDatabaseRepository.deleteTrainClassById(id)
    }

    fun deleteAllTrainClasses() {
        appDatabaseRepository.deleteAllTrainClasses()
    }


    // Queue
    /**
     * public final data class DbQueueTable(
     *     @NonNull val id: Int = 0,
     *     val targetTable: String? = null,
     *     val targetAction: String? = null,
     *     val idTarget: String? = null,
     *     val name: String? = null,
     *     val weight: Int? = null,
     *     val additionalData: String? = null
     */

    suspend fun listQueues () = appDatabaseRepository.listQueue()
    suspend fun showQueueList () = appDatabaseRepository.showListQueue()

    suspend fun countQueue () = appDatabaseRepository.countQueue()

    fun insertQueue(
        targetTable : String,
        targetAction : String,
        idTarget : String,
        name : String,
        weight : Int,
        additionalData : String
    ) {
        val queue = DbQueueTable(
            targetTable = targetTable,
            targetAction = targetAction,
            idTarget = idTarget,
            name = name,
            weight = weight,
            additionalData = additionalData
        )
        appDatabaseRepository.insertQueue(queue)
    }

    fun deleteQueueById(id: Int) {
        appDatabaseRepository.deleteQueueById(id)
    }

    fun deleteAllQueues() {
        appDatabaseRepository.deleteAllQueue()
    }


    // Ticket History
    /**
     * public final data class TicketHistoryTable(
     *     @PrimaryKey(autoGenerate = true)
     *     @NonNull
     *     var id: Int = 0,
     *     var tanggalKeberangkatan: Date? = null,
     *     var kereta: String? = null,
     *     var kelas: String? = null,
     *     var harga: Int? = null,
     *     var kotaAsal: String? = null,
     *     var stasiunAsal: String? = null,
     *     var kotaTujuan: String? = null,
     *     var stasiunTujuan: String? = null,
     *     var paketBinary: String = "0000000",
     * )
     */

    suspend fun listTicketHistory () : List<TicketHistoryTable>? {
        return appDatabaseRepository.listTicketHistory()
    }
    suspend fun upComingTicketHistory() = appDatabaseRepository.upComingTicketHistory()

    fun insertTicketHistory(
        tanggalKeberangkatan: Date,
        kereta: String,
        kelas: String,
        harga: Int,
        kotaAsal: String,
        stasiunAsal: String,
        kotaTujuan: String,
        stasiunTujuan: String,
        paketBinary: String
    ) {
        val ticketHistory = TicketHistoryTable(
            tanggalKeberangkatan = tanggalKeberangkatan,
            kereta = kereta,
            kelas = kelas,
            harga = harga,
            kotaAsal = kotaAsal,
            stasiunAsal = stasiunAsal,
            kotaTujuan = kotaTujuan,
            stasiunTujuan = stasiunTujuan,
            paketBinary = paketBinary
        )
        appDatabaseRepository.insertTicketHistory(ticketHistory)
    }

    fun deleteAllTicketHistory() {
        appDatabaseRepository.deleteAllTicketHistory()
    }



}
