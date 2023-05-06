package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MediaPlayerService.Companion.startMediaService
import com.example.myapplication.data.StationRepository
import kotlinx.coroutines.launch

class FavoritesListViewModel(
    private val stationRepository: StationRepository,
    private val application: Application) : AndroidViewModel(application) {

    private val _favoritesList = MutableLiveData<List<com.example.myapplication.model.Station>>()
    val favoritesList: LiveData<List<com.example.myapplication.model.Station>>
        get() {
            return _favoritesList
        }

    fun getSavedFavorites() {
        viewModelScope.launch {
            _favoritesList.value = stationRepository.getFavorites()
        }
    }

    fun addFavorites() {
        viewModelScope.launch {
            _favoritesList.value?.let { stationRepository.insertStations(it) }
        }
    }

    fun onFavoritesListItemClicked(favorites: com.example.myapplication.model.Station) {
        startMediaService(application, favorites.url)
    }
}