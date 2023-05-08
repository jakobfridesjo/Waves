package com.example.myapplication

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.core.os.ConfigurationCompat
import com.example.myapplication.data.AppContainer
import com.example.myapplication.data.DefaultAppContainer
import timber.log.Timber
import java.util.Locale

class Waves : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        // Setup AppContainer
        container = DefaultAppContainer(this)

        // Plant more trees
        Timber.plant(Timber.DebugTree())
    }

    fun setStationName(stationName: String) {
        
    }

    fun setStationImage(imgUrl: String) {

    }

    companion object {
        fun getAppContainer(context: Context): AppContainer {
            return (context.applicationContext as Waves).container
        }
    }
}
