package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class RecordingsViewModelFactory(private val application: Application):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RecordingsViewModel::class.java)) {
            return RecordingsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}