package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.StationRepository

@Suppress("UNCHECKED_CAST")
class MapViewModelFactory(private val movieRepository: StationRepository, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(MapViewModel::class.java)) {
            return MapViewModel(movieRepository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}