package com.example.myapplication.network

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

interface RadioBrowserApiService {

    @GET("stations/byuuid/{uuid}")
    suspend fun getStationByUUID(
        @Path("uuid") uuid: String,
        @Query("hidebroken") hideBroken: String = "true"
    ): List<Station>

    @GET("stations/topclick")
    suspend fun getTopClickedStations(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 100000,
        @Query("geo") geo: String = "true"
    ): List<Station>

    @GET("stations/topvote")
    suspend fun getTopVotedStations(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 100000,
        @Query("geo") geo: String = "true"
    ): List<Station>

    @GET("stations/search")
    suspend fun searchStationsByName(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 10000,
        @Query("geo") geo: String = "true",
        @Query("name") name: String = ""
    ): List<Station>

    @POST("url/{id}")
    suspend fun postClick(
        @Path("id") id: String,
        @Body click: Click
    ): Response<Unit>

    @POST("vote/{id}")
    suspend fun postVote(
        @Path("id") id: String,
        @Body vote: Vote
    ): Response<Unit>

}