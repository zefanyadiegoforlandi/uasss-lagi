package com.ppb.travellin.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class InformationViewModel : ViewModel() {
    val option : MutableLiveData<Int> = MutableLiveData(-1)
    var isUpdate : Boolean = false
    var targetId : String? = null

    var optionNameValue : String? = null
    var optionWeightValue : String? = null
    var optionCityValue : String? = null

    suspend fun getOption() : Int {
            return option.asFlow().first().toInt()
        }

    private suspend fun nextOption() {
        option.value = (getOption() + 1) % 3
    }

    suspend fun nextOptionString() : String {
        nextOption()
        return withContext(Dispatchers.IO) {
            when (getOption()) {
                0 -> "stations"
                1 -> "trains"
                2 -> "train_classes"
                else -> "Pilih Tipe Database"
            }
        }
    }

}