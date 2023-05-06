package com.example.myapplication

import android.app.Application
import android.content.Context
import com.example.myapplication.data.AppContainer
import com.example.myapplication.data.DefaultAppContainer
import timber.log.Timber

class Waves : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        fun getAppContainer(context: Context): AppContainer {
            return (context.applicationContext as Waves).container
        }
    }
}
