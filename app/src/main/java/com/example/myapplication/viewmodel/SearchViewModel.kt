package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.services.MediaPlayerService.Companion.startMediaService
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Click
import com.example.myapplication.model.Station
import com.example.myapplication.network.DataFetchStatus
import kotlinx.coroutines.launch

class SearchViewModel(
    private val stationRepository: StationRepository,
    private val application: Application) : AndroidViewModel(application) {

    private val _dataFetchStatus = MutableLiveData<DataFetchStatus>()
    val dataFetchStatus: LiveData<DataFetchStatus>
        get() {
            return _dataFetchStatus
        }

    private val _searchList = MutableLiveData<List<Station>>()
    val searchList: LiveData<List<Station>>
        get() {
            return _searchList
        }

    init {
        _dataFetchStatus.value = DataFetchStatus.LOADING
    }

    /**
     * Start media service if list item is clicked
     */
    fun onSearchListItemClicked(station: Station) {
        viewModelScope.launch {
            if (station.urlResolved.isNotEmpty()) {
                startMediaService(application, station.urlResolved)
            } else {
                startMediaService(application, station.url)
            }
        }
    }

    /**
     * Search for a station by name
     */
    fun searchStations(name: String) {
        viewModelScope.launch {
            try {
                _searchList.value = stationRepository.searchStationByName(name)
                _dataFetchStatus.value = DataFetchStatus.DONE
            } catch (e: Exception) {
                _dataFetchStatus.value = DataFetchStatus.ERROR
                _searchList.value = arrayListOf()
            }
        }
    }

    /**
     * Posts a click on a station
     */
    fun postClick(station: Station) {
        viewModelScope.launch {
            stationRepository.postClick(Click(station.stationUUID, 1))
        }
    }
}