package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.Setting

class SettingsViewModel (application: Application) : AndroidViewModel(application) {

    private val _settingsEvent = MutableLiveData<String>()
    val settingsEvent: LiveData<String>
        get() = _settingsEvent

    private val _settingsList = MutableLiveData<List<Setting>>()
    val settingsList: LiveData<List<Setting>>
        get() = _settingsList

    init {
        // Defines the settings list items
        _settingsList.value = listOf(
            Setting("Equalizer", "ic_equalizer"),
            /* TODO Should be able to set the maximum number of stations to fetch */
            Setting("Station count limit", "ic_search_checked")
        )
    }

    /**
     * Notify fragment
     */
    fun onSettingsListItemClicked(setting: Setting) {
        _settingsEvent.value = setting.settingName
    }

}