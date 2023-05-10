package com.example.myapplication.utils

import android.os.AsyncTask
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.Random
import java.util.Vector


object Constants {

    const val RADIO_API_BASE_HOST = "all.api.radio-browser.info"
    const val RADIO_API_BASE_URL_DEFAULT = "https://de1.api.radio-browser.info/json/"
    const val ESRI_BASE_URL = "https://server.arcgisonline.com/arcgis/rest/services/World_Imagery/MapServer/tile/"

}