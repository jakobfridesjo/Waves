package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import kotlinx.coroutines.launch

class RecordingsViewModel(private val application: Application):
    AndroidViewModel(application) {

    private val _recordingsList = MutableLiveData<List<Int>>()
    val recordingsList: LiveData<List<Int>>
        get() {
            return _recordingsList
        }

    init {
        getRecordings()
    }

    fun getRecordings() {
        viewModelScope.launch {
            _recordingsList.value = listOf(1,2,3)
        }
    }
}