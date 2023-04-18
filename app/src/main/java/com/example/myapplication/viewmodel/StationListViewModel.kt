package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.database.StationDatabaseDao
import com.example.myapplication.database.Stations
import com.example.myapplication.model.Station
import kotlinx.coroutines.launch

class StationListViewModel(private val stationDatabaseDao: StationDatabaseDao, application: Application) : AndroidViewModel(application) {
    private val _stationList = MutableLiveData<List<Station>>()
    val stationList: LiveData<List<Station>>
        get() {
            return _stationList
        }

    private val _navigateToStationDetail = MutableLiveData<Station?>()
    val navigateToStationDetail: MutableLiveData<Station?>
        get() {
            return _navigateToStationDetail
        }

    init {
        _stationList.postValue(Stations().list)
    }

    fun getSavedStations() {
        viewModelScope.launch {
            _stationList.value = stationDatabaseDao.getAllStations()
        }
    }

    fun addStation() {
        viewModelScope.launch {
            _stationList.value?.let { stationDatabaseDao.insert(it[0]) }
        }
    }

    fun onStationListItemClicked(station: Station) {
        _navigateToStationDetail.value = station
    }

    fun onStationDetailNavigated() {
        _navigateToStationDetail.value = null
    }
}