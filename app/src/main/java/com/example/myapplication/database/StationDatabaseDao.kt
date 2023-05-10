package com.example.myapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.model.Station
import com.example.myapplication.model.StationAttributes

@Dao
interface StationDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<Station>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStationAttribute(stationAttributes: StationAttributes)

    @Delete
    suspend fun deleteStations(stations: List<Station>)

    @Query("UPDATE station_attributes " +
            "SET top_clicked = :value " +
            "WHERE stationUUID IN (:stationUUID)")
    suspend fun setTopClickedStations(stationUUID: List<String>, value: Boolean)

    @Query("UPDATE station_attributes " +
            "SET top_voted = :value " +
            "WHERE stationUUID IN (:stationUUID)")
    suspend fun setTopVotedStations(stationUUID: List<String>, value: Boolean) {}

    @Query("UPDATE station_attributes SET top_clicked = 0")
    suspend fun resetTopClickedStations()

    @Query("UPDATE station_attributes SET top_voted = 0")
    suspend fun resetTopVotedStations()

    @Query("UPDATE station_attributes SET favorite = :value WHERE station_attributes.stationUUID = :stationUUID")
    suspend fun setFavoriteStation(stationUUID: String, value: Boolean)

    @Query("SELECT stations.* FROM stations " +
            "INNER JOIN station_attributes ON stations.stationUUID = station_attributes.stationUUID " +
            "WHERE station_attributes.top_clicked = 1")
    suspend fun getTopClickedStations(): List<Station>

    @Query("SELECT stations.* FROM stations " +
            "INNER JOIN station_attributes ON stations.stationUUID = station_attributes.stationUUID " +
            "WHERE station_attributes.top_voted = 1")
    suspend fun getTopVotedStations(): List<Station>

    @Query("SELECT stations.* FROM stations " +
            "INNER JOIN station_attributes ON stations.stationUUID = station_attributes.stationUUID " +
            "WHERE station_attributes.favorite = 1")
    suspend fun getFavoriteStations(): List<Station>

    @Query("SELECT * FROM stations ORDER BY name")
    suspend fun getAllStations(): List<Station>

    @Query("SELECT EXISTS(SELECT * FROM station_attributes WHERE station_attributes.stationUUID = :stationUUID)")
    suspend fun isFavorite(stationUUID: String): Boolean
}