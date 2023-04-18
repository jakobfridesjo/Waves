package com.example.myapplication.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "stations")
data class Station (
    @PrimaryKey()
    var stationuuid: String,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "favicon")
    var favicon: String,
    @ColumnInfo(name = "geo_lat")
    var geo_lat: String,
    @ColumnInfo(name = "geo_long")
    var geo_long: String
) : Parcelable