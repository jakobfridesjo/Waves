package com.example.myapplication.network

import com.example.myapplication.model.Station
import com.example.myapplication.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

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

    @GET("topclick")
    suspend fun getTopClickedStations(
        @Query("hidebroken") hideBroken: String = "false",
        @Query("limit") limit: Int = 10000,
        @Query("geo") geo: String = "true"
    ): List<Station>

    @GET("topvote")
    suspend fun getTopVotedStations(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 10000,
        @Query("geo") geo: String = "true"
    ): List<Station>

    @GET("search")
    suspend fun searchStationsByName(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 100,
        @Query("geo") geo: String = "true",
        @Query("name") name: String
    ): List<Station>
}