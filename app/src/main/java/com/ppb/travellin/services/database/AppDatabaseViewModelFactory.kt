package com.ppb.travellin.services.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AppDatabaseViewModelFactory(
    private val appDatabaseRepository: AppDatabaseRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppDatabaseViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AppDatabaseViewModel(appDatabaseRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}