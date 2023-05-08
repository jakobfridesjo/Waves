package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station

@Suppress("UNCHECKED_CAST")
class MiniplayerViewModelFactory(
    private val station: Station,
    private val stationRepository: StationRepository,
    private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MiniplayerViewModel::class.java)) {
            return MiniplayerViewModel(station, stationRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}