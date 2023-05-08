package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MediaPlayerService
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import com.ltu.m7019e.v23.themoviedb.network.DataFetchStatus
import kotlinx.coroutines.launch


class MiniplayerViewModel(
    private val stationArg: Station,
    private val stationRepository: StationRepository,
    application: Application) : AndroidViewModel(application) {

    private val _station = MutableLiveData<Station>()
    val station: LiveData<Station>
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
            _isFavorite.value = stationRepository.isFavorite(stationArg)
            // TODO
            _isPlaying.value = true
        }
    }

    fun onSaveStation(station: Station) {
        viewModelScope.launch {
            stationRepository.insertStations(listOf(station))
        }
        setIsFavorite(station)
    }

    fun onDeleteStation(station: Station) {
        viewModelScope.launch {
            stationRepository.deleteStations(listOf(station))
        }
    }

    private fun setIsFavorite(station: Station) {
        viewModelScope.launch {
            _isFavorite.value = stationRepository.isFavorite(station)
        }
        setIsFavorite(station)
    }

    fun stopPlayer() {
        viewModelScope.launch {
            _isPlaying.value = false
        }
    }

    fun startPlayer() {
        viewModelScope.launch {
            _isPlaying.value = true
        }
    }
}