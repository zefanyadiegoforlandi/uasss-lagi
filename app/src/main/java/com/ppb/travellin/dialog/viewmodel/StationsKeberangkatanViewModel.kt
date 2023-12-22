package com.ppb.travellin.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ppb.travellin.services.database.stations.StationsTable

class StationsKeberangkatanViewModel : ViewModel() {
    var data = MutableLiveData<StationsTable>()
}