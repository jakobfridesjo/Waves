package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import kotlinx.coroutines.launch
import com.example.myapplication.network.DataFetchStatus

class MapViewModel(
    private val stationRepository: StationRepository,
    application: Application) : AndroidViewModel(application) {

    private val _stationList = MutableLiveData<List<Station>>()
    val stationList: LiveData<List<Station>>
        get() {
            return _stationList
        }

    private val _station = MutableLiveData<Station>()
    val station: LiveData<Station>
        get() {
            return _station
        }

    private val _advice = MutableLiveData<String>()
    val advice: LiveData<String>
        get() {
            return _advice
        }

    init {
        getTopStations()
    }

    /**
     * Gets the top stations
     */
    private fun getTopStations() {
        viewModelScope.launch {
            try {
                _stationList.value = stationRepository.getTopVoted()
            } catch (e: Exception) {
                _stationList.value = arrayListOf()
            }
        }
    }

    /**
     * Gets data for an individual station
     */
    fun getStation(stationUUID: String) {
        viewModelScope.launch {
            try {
                _station.value = stationRepository.getStation(stationUUID)[0]
            } catch (e: java.lang.Exception) {
                //TODO Do something here
            }
        }
    }

    /**
     * Gets a random advice
     */
    fun getAdvice() {
        viewModelScope.launch {
            try {
                _advice.value = stationRepository.getAdvice()
            } catch (e: java.lang.Exception) {
                //TODO Do something here
            }
        }
    }
}