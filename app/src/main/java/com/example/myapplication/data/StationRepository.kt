package com.example.myapplication.data

import com.example.myapplication.database.StationDatabaseDao
import com.example.myapplication.model.Click
import com.example.myapplication.model.Station
import com.example.myapplication.model.StationAttributes
import com.example.myapplication.model.Vote
import com.example.myapplication.network.AdviceSlipApiService
import com.example.myapplication.network.RadioBrowserApiService
import timber.log.Timber

interface StationRepository {

    suspend fun getTopVoted(): List<Station>
    suspend fun getTopClicked(): List<Station>
    suspend fun getFavorites(): List<Station>
    suspend fun searchStationByName(name: String): List<Station>
    suspend fun deleteStations(stations: List<Station>)
    suspend fun insertStations(stations: List<Station>)
    suspend fun getStation(stationUUID: String): List<Station>
    suspend fun isFavorite(stationUUID: String): Boolean
    suspend fun postVote(vote: Vote)
    suspend fun postClick(click: Click)

    suspend fun getAdvice(): String

}

class DefaultStationRepository(
    private val stationDatabaseDao: StationDatabaseDao,
    private val radioBrowserApiService: RadioBrowserApiService,
    private val adviceSlipApiService: AdviceSlipApiService
    ) : StationRepository {


    /**
     * Gets the station by UUID from the Radio Browser API
     */
    override suspend fun getStation(stationUUID: String): List<Station> {
        return radioBrowserApiService.getStationByUUID(stationUUID)
    }

    /**
     * Get the top voted stations from the Radio Browser API
     */
    override suspend fun getTopVoted(): List<Station> {
        try {
            return radioBrowserApiService.getTopVotedStations()
        } catch (exception: Exception) {
            println(exception.message)
            Timber.tag("STATION_REPOSITORY_TOP_VOTED").d("NETWORK FETCH FAILED")
        }
        return emptyList()
    }

    /**
     * Get the top clicked stations from the Radio Browser API
     */
    override suspend fun getTopClicked(): List<Station> {
        try {
            return radioBrowserApiService.getTopClickedStations()
        } catch (exception: Exception) {
            Timber.tag("STATION_REPOSITORY_TOP_CLICKED").d("NETWORK FETCH FAILED")
        }
        return emptyList()
    }

    /**
     * Gets favorite stations from local database
     */
    override suspend fun getFavorites(): List<Station> {
        return stationDatabaseDao.getFavoriteStations()
    }

    /**
     * Search a RadioBrowser API station by it's name
     */
    override suspend fun searchStationByName(name: String): List<Station> {
        try {
            return radioBrowserApiService.searchStationsByName("true", 1000, "true", name)
        } catch (exception: Exception) {
            Timber.tag("STATION_REPOSITORY_SEARCH").d("NETWORK FETCH FAILED")
        }
        return emptyList()
    }

    /**
     * Remove a station from the local database
     */
    override suspend fun deleteStations(stations: List<Station>) {
        stationDatabaseDao.deleteStations(stations)
        stations.forEach {
            stationDatabaseDao.setFavoriteStation(it.stationUUID, false)
        }
    }

    /**
     * Insert a station
     */
    override suspend fun insertStations(stations: List<Station>) {
        stationDatabaseDao.insertStations(stations)
        stations.forEach {
            stationDatabaseDao.insertStationAttribute(StationAttributes(it.stationUUID,
                topClicked = false,
                topVoted = false,
                favorite = false
                )
            )
            stationDatabaseDao.setFavoriteStation(it.stationUUID, true)
        }
    }

    /**
     * Checks if a RadioBrowser API station is set as favorite
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
     * Posts a click on a RadioBrowser API station
     */
    override suspend fun postClick(click: Click) {
        radioBrowserApiService.postClick(click.id, click)
    }

    /**
     * Gets a random advice from the AdviceSlip API
     */
    override suspend fun getAdvice(): String {
        return adviceSlipApiService.getAdviceSlip().slip.advice
    }

}