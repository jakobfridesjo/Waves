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

    /**
     * Inserts a station, replace if station already exists
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(stations: List<Station>)

    /**
     * Inserts attributes for a station, don't do anything
     * if the attribute exists
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStationAttribute(stationAttributes: StationAttributes)

    /**
     * Delete stations, wrap a single station in a list for single station removal
     */
    @Delete
    suspend fun deleteStations(stations: List<Station>)

    /**
     * Sets the stations as top clicked
     */
    @Query("UPDATE station_attributes " +
            "SET top_clicked = :value " +
            "WHERE stationUUID IN (:stationUUID)")
    suspend fun setTopClickedStations(stationUUID: List<String>, value: Boolean)

    /**
     * Sets the stations as top voted
     */
    @Query("UPDATE station_attributes " +
            "SET top_voted = :value " +
            "WHERE stationUUID IN (:stationUUID)")
    suspend fun setTopVotedStations(stationUUID: List<String>, value: Boolean) {}

    /**
     * Resets all top clicked attributes
     */
    @Query("UPDATE station_attributes SET top_clicked = 0")
    suspend fun resetTopClickedStations()

    /**
     * Reset all top voted attributes
     */
    @Query("UPDATE station_attributes SET top_voted = 0")
    suspend fun resetTopVotedStations()

    /**
     * Set a station as favorite
     */
    @Query("UPDATE station_attributes SET favorite = :value WHERE station_attributes.stationUUID = :stationUUID")
    suspend fun setFavoriteStation(stationUUID: String, value: Boolean)

    /**
     * Get the top clicked stations
     */
    @Query("SELECT stations.* FROM stations " +
            "INNER JOIN station_attributes ON stations.stationUUID = station_attributes.stationUUID " +
            "WHERE station_attributes.top_clicked = 1")
    suspend fun getTopClickedStations(): List<Station>

    /**
     * Get the top voted stations
     */
    @Query("SELECT stations.* FROM stations " +
            "INNER JOIN station_attributes ON stations.stationUUID = station_attributes.stationUUID " +
            "WHERE station_attributes.top_voted = 1")
    suspend fun getTopVotedStations(): List<Station>

    /**
     * Gets the favorite stations
     */
    @Query("SELECT stations.* FROM stations " +
            "INNER JOIN station_attributes ON stations.stationUUID = station_attributes.stationUUID " +
            "WHERE station_attributes.favorite = 1")
    suspend fun getFavoriteStations(): List<Station>

    /**
     * Checks if a station is set as favorite
     */
    @Query("SELECT EXISTS(SELECT * FROM station_attributes WHERE station_attributes.stationUUID = :stationUUID)")
    suspend fun isFavorite(stationUUID: String): Boolean
}