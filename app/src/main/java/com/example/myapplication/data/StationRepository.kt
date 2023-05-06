package com.example.myapplication.data

import com.example.myapplication.database.StationDatabaseDao
import com.example.myapplication.model.Station
import com.example.myapplication.network.RadioBrowserApiService
import timber.log.Timber

interface StationRepository {

    suspend fun getTopVoted(): List<Station>
    suspend fun getTopClicked(): List<Station>
    suspend fun getFavorites(): List<Station>
    suspend fun searchStationByName(name: String): List<Station>
    suspend fun deleteStations(stations: List<Station>)
    suspend fun insertStations(stations: List<Station>)
    suspend fun setFavorite(station: Station, value: Boolean)

}

class DefaultStationRepository(private val stationDatabaseDao: StationDatabaseDao, private val radioBrowserApiService: RadioBrowserApiService) : StationRepository {

    /**
     * Get the top voted stations
     */
    override suspend fun getTopVoted(): List<Station> {
        try {
            val stations = radioBrowserApiService.getTopVotedStations("true", 100000, "true")
            stationDatabaseDao.insertStations(stations)
            stationDatabaseDao.resetTopVotedStations()
            stationDatabaseDao.setTopVotedStations(stations.map {it.stationUUID}, true)
            return stations
        } catch (exception: Exception) {
            println(exception.message)
            Timber.tag("STATION_REPOSITORY_TOP_VOTED").d("NETWORK FETCH FAILED, USING LOCAL DATA")
        }
        return stationDatabaseDao.getTopVotedStations()
    }

    /**
     * Get the top clicked stations
     */
    override suspend fun getTopClicked(): List<Station> {
        try {
            val stations = radioBrowserApiService.getTopClickedStations("true", 100000, "true")
            stationDatabaseDao.insertStations(stations)
            stationDatabaseDao.resetTopClickedStations()
            stationDatabaseDao.setTopClickedStations(stations.map {it.stationUUID}, true)
            return stations
        } catch (exception: Exception) {
            Timber.tag("STATION_REPOSITORY_TOP_CLICKED").d("NETWORK FETCH FAILED, USING LOCAL DATA")
        }
        return stationDatabaseDao.getTopClickedStations()
    }

    /**
     * Gets favorite stations
     */
    override suspend fun getFavorites(): List<Station> {
        return stationDatabaseDao.getFavoriteStations()
    }

    /**
     * Search a station by it's name
     */
    override suspend fun searchStationByName(name: String): List<Station> {
        try {
            return radioBrowserApiService.searchStationsByName("true", 1000, "true", name)
        } catch (exception: Exception) {
            Timber.tag("STATION_REPOSITORY_TOP_CLICKED").d("NETWORK FETCH FAILED, USING LOCAL DATA")
        }
        return emptyList()
    }

    override suspend fun setFavorite(station: Station, value: Boolean) {
        stationDatabaseDao.setFavoriteStation(station.stationUUID, value)
    }

    /**
     * Remove a station
     */
    override suspend fun deleteStations(stations: List<Station>) {
        stationDatabaseDao.deleteStations(stations)
    }

    /**
     * Insert a station
     */
    override suspend fun insertStations(stations: List<Station>) {
        stationDatabaseDao.insertStations(stations)
    }
}