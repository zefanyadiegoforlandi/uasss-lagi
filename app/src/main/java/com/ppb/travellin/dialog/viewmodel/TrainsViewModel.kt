package com.ppb.travellin.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ppb.travellin.services.database.trains.TrainsTable

class TrainsViewModel : ViewModel() {
    var data = MutableLiveData<TrainsTable>()
}