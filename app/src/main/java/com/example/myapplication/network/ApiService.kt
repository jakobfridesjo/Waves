package com.example.myapplication.network

import com.example.myapplication.model.AdviceSlip
import com.example.myapplication.model.Click
import com.example.myapplication.model.Station
import com.example.myapplication.model.Vote
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Add a httpclient logger for debugging purpose
 * object.
 */
fun getLoggerIntercepter(): HttpLoggingInterceptor {
    val logging = HttpLoggingInterceptor()
    logging.level = HttpLoggingInterceptor.Level.BODY
    return logging
}

/**
 * Radio Browser API service
 */
interface RadioBrowserApiService {

    /**
     * Get stations by known uuid
     * uuid: String (in UUID format) - UUID for station to get
     * hidebroken: Boolean - hide broken stations
     */
    @GET("stations/byuuid/{uuid}")
    suspend fun getStationByUUID(
        @Path("uuid") uuid: String,
        @Query("hidebroken") hideBroken: String = "true"
    ): List<Station>

    /**
     * Get top clicked stations
     * hidebroken: Boolean - hide broken stations
     * limit: Int - maxmimal number of stations returned
     * geo: Boolean - if station has to have a geolocation
     */
    @GET("stations/topclick")
    suspend fun getTopClickedStations(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 25000,
        @Query("geo") geo: String = "true"
    ): List<Station>

    /**
     * Get top voted stations
     * hidebroken: Boolean - hide broken stations
     * limit: Int - maxmimal number of stations returned
     * geo: Boolean - if station has to have a geolocation
     */
    @GET("stations/topvote")
    suspend fun getTopVotedStations(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 25000,
        @Query("geo") geo: String = "true"
    ): List<Station>

    /**
     * Search for a station
     * hidebroken: Boolean - hide broken stations
     * limit: Int - maxmimal number of stations returned
     * geo: Boolean - if station has to have a geolocation
     * name: String - name to search for
     */
    @GET("stations/search")
    suspend fun searchStationsByName(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 10000,
        @Query("geo") geo: String = "true",
        @Query("name") name: String = ""
    ): List<Station>

    /**
     * Clicks a station (makes it rank higher in popularity)
     * id: String (in UUID format) - UUID for station to get
     * See the Click.kt model in the model package
     */
    @POST("url/{id}")
    suspend fun postClick(
        @Path("id") id: String,
        @Body click: Click
    ): Response<Unit>

    /**
     * Sends a vote to the Radio Browser API
     * id: String (in UUID format) - UUID for station to get
     * vote: See the Vote.kt model in the model package
     */
    @POST("vote/{id}")
    suspend fun postVote(
        @Path("id") id: String,
        @Body vote: Vote
    ): Response<Unit>
}

/**
 * Advice Slip API service
 */
interface AdviceSlipApiService {

    /**
     * Gets an advice
     */
    @GET("advice")
    suspend fun getAdviceSlip(): AdviceSlip
}