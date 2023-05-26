package com.example.myapplication.viewmodel

import android.app.Application
import android.media.MediaMetadataRetriever
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.Recording
import com.example.myapplication.util.Constants
import kotlinx.coroutines.launch
import java.io.File

class RecordingsViewModel(application: Application):
    AndroidViewModel(application) {

    private val _recordingsList = MutableLiveData<List<Recording>>()
    val recordingsList: LiveData<List<Recording>>
        get() {
            return _recordingsList
        }

    /**
     * Gets the recordings
     */
    fun getRecordings() {
        viewModelScope.launch {
            val recordings = mutableListOf<Recording>()

            val directory = File(Constants.RECORDINGS_PATH)
            println(directory.toString())
            if (directory.exists() && directory.isDirectory) {
                val files = directory.listFiles()
                files?.forEach { file ->
                    val filename = file.name
                    val filePath = file.absolutePath
                    val clipLength: Long = getClipLengthFromFile(file)
                    val recording = Recording(filename, filePath, clipLength)
                    recordings.add(recording)
                }
            }

            _recordingsList.value = recordings
        }
    }

    /**
     * Get the length of a recorded clip
     */
    private fun getClipLengthFromFile(file: File): Long {
        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(file.absolutePath)
            val durationString =
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            val duration = durationString?.toLongOrNull() ?: 0L
            retriever.release()
            return duration
        }
        catch (exception: Exception) {
            println(exception.message)
        }
        return 0
    }

}