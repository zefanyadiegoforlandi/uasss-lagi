package com.ppb.travellin.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ppb.travellin.services.database.stations.StationsTable
import com.ppb.travellin.services.database.train_classes.TrainClassesTable
import com.ppb.travellin.services.database.trains.TrainsTable

class GlobalViewModel<T> : ViewModel() {
    var data = MutableLiveData<T>()
}

class StationsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GlobalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GlobalViewModel<StationsTable>() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TrainClassesViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GlobalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GlobalViewModel<TrainClassesTable>() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class TrainsViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GlobalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GlobalViewModel<TrainsTable>() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}