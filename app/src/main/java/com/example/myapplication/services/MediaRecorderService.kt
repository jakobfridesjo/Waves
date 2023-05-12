package com.example.myapplication.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class MediaRecorderService : Service() {

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_RECORDING -> {
                return START_STICKY
            }
            ACTION_STOP_RECORDING -> {

            }
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    companion object {

        private var mMediaRecorderIntent: Intent? = null

        const val ACTION_START_RECORDING = "com.example.myapplication.ACTION_START_RECORDING"
        const val ACTION_STOP_RECORDING = "com.example.myapplication.ACTION_STOP_RECORDING"
        const val EXTRA_SOURCE_URL = "com.example.myapplication.EXTRA_FILE_NAME"
        const val EXTRA_SAMPLE_RATE = "com.example.myapplication.EXTRA_SAMPLE_RATE"
        private const val DEFAULT_SAMPLE_RATE = 48000

        fun startRecordingService(
            context: Context,
            fileName: String,
            sampleRate: Int = DEFAULT_SAMPLE_RATE
        ) {
            mMediaRecorderIntent = Intent(context, MediaRecorderService::class.java).apply {
                action = ACTION_START_RECORDING
                putExtra(EXTRA_SOURCE_URL, fileName)
                putExtra(EXTRA_SAMPLE_RATE, sampleRate)
            }
            context.startService(mMediaRecorderIntent)
        }

        fun stopRecordingService(context: Context) {
            mMediaRecorderIntent = Intent(context, MediaRecorderService::class.java).apply {
                action = ACTION_STOP_RECORDING
            }
            context.startService(mMediaRecorderIntent)
        }
    }
}
