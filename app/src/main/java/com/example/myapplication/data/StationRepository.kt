package com.example.myapplication.data

import com.example.myapplication.database.StationDatabaseDao
import com.example.myapplication.model.Click
import com.example.myapplication.model.Station
import com.example.myapplication.model.Vote
import com.example.myapplication.network.RadioBrowserApiService
import timber.log.Timber

interface StationRepository {

    suspend fun getTopVoted(): List<Station>
    suspend fun getTopClicked(): List<Station>
    suspend fun getFavorites(): List<Station>
    suspend fun searchStationByName(name: String): List<Station>
    suspend fun deleteStations(stations: List<Station>)
    suspend fun insertStations(stations: List<Station>)
    suspend fun setFavorite(stationUUID: String, value: Boolean)
    suspend fun getStation(stationUUID: String): List<Station>
    suspend fun isFavorite(stationUUID: String): Boolean
    suspend fun postVote(vote: Vote)
    suspend fun postClick(click: Click)

}

class DefaultStationRepository(private val stationDatabaseDao: StationDatabaseDao, private val radioBrowserApiService: RadioBrowserApiService) : StationRepository {


    /**
     * Gets the station by UUID
     */
    override suspend fun getStation(stationUUID: String): List<Station> {
        return radioBrowserApiService.getStationByUUID(stationUUID)
    }

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

    override suspend fun setFavorite(stationUUID: String, value: Boolean) {
        stationDatabaseDao.setFavoriteStation(stationUUID, value)
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

    /**
     * Checks if a station is favorited
     */
    override suspend fun isFavorite(stationUUID: String): Boolean {
        return stationDatabaseDao.isFavorite(stationUUID)
    }

    /**
     * Posts a vote to a station
     */
    override suspend fun postVote(vote: Vote) {
        radioBrowserApiService.postVote(vote.id, vote)
    }

    /**
     * Posts a click on a station
     */
    override suspend fun postClick(click: Click) {
        radioBrowserApiService.postClick(click.id, click)
    }

}