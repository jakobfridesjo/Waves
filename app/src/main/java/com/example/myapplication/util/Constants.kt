package com.example.myapplication.util

import android.content.Context
import android.media.AudioFormat
import android.media.AudioRecord
import android.os.Build
import android.os.Environment
import androidx.annotation.RequiresApi

object Constants {

    const val RADIO_API_BASE_HOST = "all.api.radio-browser.info"
    const val RADIO_API_BASE_URL_DEFAULT = "https://de1.api.radio-browser.info/json/"
    const val ESRI_API_BASE_URL = "https://server.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/"
    const val ADVICE_SLIP_API_BASE_URL = "https://api.adviceslip.com/"
    const val RECORD_AUDIO_PERMISSION_REQUEST_CODE: Int = 523659898
    const val SAMPLE_RATE = 48000
    const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_STEREO
    const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_8BIT
    const val BUFFER_SIZE = 1024
    lateinit var RECORDINGS_PATH: String
}