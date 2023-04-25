package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Station
import com.example.myapplication.network.RBApi
import com.ltu.m7019e.v23.themoviedb.network.DataFetchStatus
import kotlinx.coroutines.launch

class MapViewModel(private val application: Application) : AndroidViewModel(application) {

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

    init {
        getTopStations()
        _dataFetchStatus.value = DataFetchStatus.LOADING
    }

    fun getTopStations() {
        viewModelScope.launch {
            try {
                _stationList.value = RBApi.radioListRetrofitService.getTopVotedStations()
                _dataFetchStatus.value = DataFetchStatus.DONE
            } catch (e: Exception) {
                _dataFetchStatus.value = DataFetchStatus.ERROR
                _stationList.value = arrayListOf()
            }
        }
    }
}