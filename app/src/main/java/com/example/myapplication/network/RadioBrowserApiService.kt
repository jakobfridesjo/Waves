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
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


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
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val stationListRetrofit = Retrofit.Builder()
    .client(
        OkHttpClient.Builder()
            .addInterceptor(getLoggerIntercepter())
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    )
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.RADIO_API_BASE_URL)
    .build()

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
    suspend fun searchStations(
        @Query("hidebroken") hideBroken: String = "true",
        @Query("limit") limit: Int = 10,
        @Query("geo") geo: String = "true",
        @Query("name") name: String
    ): List<Station>
}

object RBApi {
    val radioListRetrofitService: RadioBrowserApiService by lazy { stationListRetrofit.create(RadioBrowserApiService::class.java)}
}