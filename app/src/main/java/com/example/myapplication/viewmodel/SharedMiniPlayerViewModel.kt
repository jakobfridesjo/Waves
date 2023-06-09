package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import com.example.myapplication.service.MediaPlayerService.Companion.startMediaService
import com.example.myapplication.service.MediaPlayerService.Companion.stopMediaService
import com.example.myapplication.service.MediaRecorderService.Companion.startMediaRecorderService
import com.example.myapplication.service.MediaRecorderService.Companion.stopMediaRecorderService
import kotlinx.coroutines.launch


class SharedMiniPlayerViewModel(
    private val stationRepository: StationRepository,
    private val application: Application) :
    AndroidViewModel(application) {

    private val _station = MutableLiveData<List<Station>>()
    val station: LiveData<List<Station>>
        get() {
            return _station
        }

    private val _isFavorite = MutableLiveData<Boolean>(false)
    val isFavorite: LiveData<Boolean>
        get() = _isFavorite

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _isRecording = MutableLiveData<Boolean>(false)
    val isRecording: LiveData<Boolean>
        get() = _isRecording

    private val _isEnabled = MutableLiveData<Boolean>(false)
    val isEnabled: LiveData<Boolean>
        get() = _isEnabled

    /**
     * Saves a station to the local database
     */
    fun saveStation(station: Station) {
        viewModelScope.launch {
            stationRepository.insertStations(listOf(station))
        }
        _isFavorite.value = true
    }

    /**
     * Deletes a station from the local database
     */
    fun deleteStation(station: Station) {
        viewModelScope.launch {
            stationRepository.deleteStations(listOf(station))
        }
        _isFavorite.value = false
    }

    /**
     * Stops the media player service
     */
    fun stopPlayer() {
        _isPlaying.value = false
        stopMediaService(application)
    }

    /**
     * Starts the media player service and plays the station
     */
    fun startPlayer(station: Station) {
        _station.value = listOf(station)
        _isPlaying.value = true
        _isEnabled.value = true
        viewModelScope.launch {
            _isFavorite.value = stationRepository.isFavorite(station.stationUUID)
        }
        startMediaService(application, station.urlResolved)
    }

    fun stopRecording() {
        stopMediaRecorderService(application)
        _isRecording.value = false
    }


    fun startRecording() {
        startMediaRecorderService(application)
        _isRecording.value = true
    }

    /**
     * Show the mini player
     */
    fun setVisible() {
        _isEnabled.value = true
    }

    /**
     * Hide the mini player
     */
    fun setInvisible() {
        _isEnabled.value = false
    }
}