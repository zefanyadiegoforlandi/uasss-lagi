package com.ppb.travellin.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ppb.travellin.services.database.train_classes.TrainClassesTable

class TrainClassesViewModel : ViewModel() {
    var data = MutableLiveData<TrainClassesTable>()
}