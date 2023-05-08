package com.example.myapplication.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class NowPlaying (

    @Json(name = "stationuuid")
    var stationUUID: String = "",

    @Json(name = "stationname")
    var stationName: String = "",

    @Json(name = "title")
    var title: String = "",

    @Json(name = "artist")
    var artist: String = ""

) : Parcelable