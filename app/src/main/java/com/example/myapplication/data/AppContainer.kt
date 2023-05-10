package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.database.StationDatabase
import com.example.myapplication.database.StationDatabaseDao
import com.example.myapplication.network.RadioBrowserApiService
import com.example.myapplication.network.getLoggerIntercepter
import com.example.myapplication.utils.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.InetAddress
import java.util.concurrent.TimeUnit

interface AppContainer {
    val stationDatabaseDao: StationDatabaseDao
    val stationRepository: StationRepository
}

class DefaultAppContainer(context: Context) : AppContainer {

    private val stationDatabase =  StationDatabase.getInstance(context)

    /**
     * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
     * full Kotlin compatibility.
     */
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Gets the base url by a dns lookuph
     */
    private fun getRadioApiBaseUrl(): String {
        return try {
            val inetAddresses = InetAddress.getAllByName(Constants.RADIO_API_BASE_HOST)
            val addresses = inetAddresses.mapNotNull { it.hostAddress }
            addresses.shuffled().first()
        } catch (e: Exception) {
            Constants.RADIO_API_BASE_URL_DEFAULT
        }
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
        .baseUrl(getRadioApiBaseUrl())
        .build()

    /**
     * Setup retrofit service
     */
    private val retrofitService: RadioBrowserApiService by lazy {
        stationListRetrofit.create(RadioBrowserApiService::class.java)
    }

    override val stationDatabaseDao: StationDatabaseDao
        get() = stationDatabase.stationDatabaseDao

    override val stationRepository: StationRepository by lazy {
        DefaultStationRepository (
            stationDatabaseDao,
            retrofitService
        )
    }

}