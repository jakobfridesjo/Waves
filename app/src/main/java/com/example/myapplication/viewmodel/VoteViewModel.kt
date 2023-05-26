package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.StationRepository
import com.example.myapplication.model.Station
import com.example.myapplication.model.Vote
import kotlinx.coroutines.launch
import com.example.myapplication.network.DataFetchStatus

class VoteViewModel(
    private val stationRepository: StationRepository,
    private val application: Application) : AndroidViewModel(application) {

    private val _station = MutableLiveData<Station>()
    val station: LiveData<Station>
        get() {
            return _station
        }

    /**
     * Posts a vote on a station
     */
    fun postVote(vote: Vote) {
        viewModelScope.launch {
            stationRepository.postVote(vote)
        }
    }
}