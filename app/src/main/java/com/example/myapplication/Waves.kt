package com.example.myapplication

import android.app.Application
import android.content.Context
import android.os.Environment
import com.example.myapplication.data.AppContainer
import com.example.myapplication.data.DefaultAppContainer
import com.example.myapplication.util.Constants
import timber.log.Timber
import com.google.android.material.color.DynamicColors;

class Waves : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()

        // Setup AppContainer
        container = DefaultAppContainer(this)

        // Set path for recordings
        Constants.RECORDINGS_PATH = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC)?.absolutePath.toString()

        // Apply colors dynamically
        DynamicColors.applyToActivitiesIfAvailable(this);

        // Plant more trees
        Timber.plant(Timber.DebugTree())
    }

    companion object {
        fun getAppContainer(context: Context): AppContainer {
            return (context.applicationContext as Waves).container
        }
    }
}
