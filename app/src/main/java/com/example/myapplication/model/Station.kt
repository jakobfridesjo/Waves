package com.example.myapplication.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stations")
data class Station (

    @Json()
    @PrimaryKey
    var stationuuid: String,

    @Json
    @ColumnInfo(name = "name")
    var name: String,

    @Json
    @ColumnInfo(name = "url")
    var url: String,

    @Json
    @ColumnInfo(name = "favicon")
    var favicon: String? = "",

    @Json
    @ColumnInfo(name = "geo_lat")
    var geo_lat: Double? = null,

    @Json
    @ColumnInfo(name = "geo_long")
    var geo_long: Double? = null

) : Parcelable