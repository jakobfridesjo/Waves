package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.MediaPlayerService.Companion.startMediaService
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import com.ltu.m7019e.v23.themoviedb.network.DataFetchStatus
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

    fun onSearchListItemClicked(station: Station) {
        startMediaService(application, station.url)
    }

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
}