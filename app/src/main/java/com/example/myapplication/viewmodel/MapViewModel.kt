package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import com.ltu.m7019e.v23.themoviedb.network.DataFetchStatus
import kotlinx.coroutines.launch
import timber.log.Timber

class MapViewModel(
    private val stationRepository: StationRepository,
    application: Application) : AndroidViewModel(application) {

    private val _dataFetchStatus = MutableLiveData<DataFetchStatus>()
    val dataFetchStatus: LiveData<DataFetchStatus>
        get() {
            return _dataFetchStatus
        }

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

    init {
        getTopStations()
        _dataFetchStatus.value = DataFetchStatus.LOADING
    }

    /**
     * Gets the top stations
     */
    fun getTopStations() {
        viewModelScope.launch {
            try {
                _stationList.value = stationRepository.getTopVoted()
                _dataFetchStatus.value = DataFetchStatus.DONE
            } catch (e: Exception) {
                _dataFetchStatus.value = DataFetchStatus.ERROR
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
                println("TESTING STUFF")
                _station.value = stationRepository.getStation(stationUUID)[0]
                println(station)
            } catch (e: java.lang.Exception) {
                println("GETTING STATION FAILED")
            }
        }
    }
}