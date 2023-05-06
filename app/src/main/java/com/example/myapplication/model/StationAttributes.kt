package com.example.myapplication.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "station_attributes")
data class StationAttributes (

    @PrimaryKey
    var stationUUID: String = "",

    @ColumnInfo(name = "top_clicked")
    var topClicked: Boolean = false,

    @ColumnInfo(name = "top_voted")
    var topVoted: Boolean = false,

    @ColumnInfo(name = "favorite")
    var favorite: Boolean = false

) : Parcelable