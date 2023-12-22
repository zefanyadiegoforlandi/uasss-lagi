package com.ppb.travellin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SystemThemeViewModel : ViewModel() {
    val themeMode = MutableLiveData<Int>()
    val switchState = MutableLiveData<Boolean>()
    val switchLabel = MutableLiveData<String>()

}