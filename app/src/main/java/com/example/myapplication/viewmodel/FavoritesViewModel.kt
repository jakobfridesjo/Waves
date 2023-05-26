package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val stationRepository: StationRepository,
    application: Application) : AndroidViewModel(application) {

    private val _favoritesList = MutableLiveData<List<Station>>()
    val favoritesList: LiveData<List<Station>>
        get() {
            return _favoritesList
        }

    init {
        getSavedFavorites()
    }

    /**
     * Get all saved favorite stations
     */
    fun getSavedFavorites() {
        viewModelScope.launch {
            _favoritesList.value = stationRepository.getFavorites()
        }
    }
}