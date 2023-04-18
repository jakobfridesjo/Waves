package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.database.StationDatabaseDao

@Suppress("UNCHECKED_CAST")
class StationListViewModelFactory(private val stationDatabaseDao: StationDatabaseDao,
                                private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StationListViewModel::class.java)) {
            return StationListViewModel(stationDatabaseDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}