package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MediaPlayerService.Companion.startMediaService
import com.example.myapplication.MediaPlayerService.Companion.stopMediaService
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import kotlinx.coroutines.launch


class MiniplayerViewModel(
    private val stationArg: Station,
    private val stationRepository: StationRepository,
    private val application: Application) : AndroidViewModel(application) {

    private val _station = MutableLiveData<List<Station>>()
    val station: LiveData<List<Station>>
        get() {
            return _station
        }

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    init {
        viewModelScope.launch {
            _isFavorite.value = stationRepository.isFavorite(stationArg.stationUUID)
            // TODO
            _isPlaying.value = true
        }
    }

    fun onSaveStation(stationUUID: String) {
        viewModelScope.launch {
            val station = stationRepository.getStation(stationUUID)
            stationRepository.insertStations(station)
        }
        viewModelScope.launch {
            stationRepository.setFavorite(stationUUID, true)
        }
        setIsFavorite(stationUUID)
    }

    fun onDeleteStation(stationUUID: String) {
        viewModelScope.launch {
            val station = stationRepository.getStation(stationUUID)
            stationRepository.deleteStations(station)
        }
        viewModelScope.launch {
            stationRepository.setFavorite(stationUUID, false)
        }
        setIsFavorite(stationUUID)
    }

    private fun setIsFavorite(stationUUID: String) {
        viewModelScope.launch {
            _isFavorite.value = stationRepository.isFavorite(stationUUID)
        }
    }

    fun stopPlayer() {
        viewModelScope.launch {
            _isPlaying.value = false
            stopMediaService(application)
        }
    }

    fun startPlayer(station: Station) {
        viewModelScope.launch {
            _isPlaying.value = true
            startMediaService(application, station.urlResolved)
        }
    }
}