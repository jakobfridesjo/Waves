package com.example.myapplication.service

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.example.myapplication.util.Constants
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MediaRecorderService : Service() {

    private lateinit var audioRecord: AudioRecord
    private var isRecording: Boolean = false

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START_RECORDING -> {
                startRecording()
                return START_STICKY
            }

            ACTION_STOP_RECORDING -> {
                stopRecording()
                stopSelf()
            }
        }
        return START_NOT_STICKY
    }

    /**
     * I don't know what to do here, so return null for now
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Start recording the sound output
     */
    private fun startRecording() {
        println("START RECORDING")
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("RETURNING, NO PERMISSIONS")
            return
        }

        // Audio properties are configured in the utils/Constants.kt file
        audioRecord = AudioRecord(
            MediaRecorder.AudioSource.UNPROCESSED,
            Constants.SAMPLE_RATE,
            Constants.CHANNEL_CONFIG,
            Constants.AUDIO_FORMAT,
            Constants.BUFFER_SIZE
        )
        audioRecord.startRecording()
        isRecording = true

        Thread {
            writeAudioData()
        }.start()
    }

    private fun writeAudioData() {
        // Create a file to save the recorded data
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "recording_$timeStamp.pcm"
        val filePath = Constants.RECORDINGS_PATH + '/' + fileName

        val data = ByteArray(Constants.BUFFER_SIZE / 2)
        var outputStream: FileOutputStream? = null
        try {
            outputStream = FileOutputStream(filePath)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        while (isRecording) {
            val read = audioRecord.read(data, 0, data.size)
            try {
                outputStream?.write(data, 0, read)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

        try {
            outputStream?.flush()
            outputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Clean up
        audioRecord.stop()
        audioRecord.release()
        println("STOP RECORDING")
    }

    /**
     * Stop recording the sound output
     */
    private fun stopRecording() {
        isRecording = false
    }

    companion object {
        const val ACTION_START_RECORDING: String = "com.example.myapplication.action.START_RECORDING"
        const val ACTION_STOP_RECORDING: String = "com.example.myapplication.action.STOP_RECORDING"

        /**
         * Start audio recording service
         */
        fun startMediaRecorderService(context: Context) {
            val recordingIntent = Intent(context, MediaRecorderService::class.java).apply {
                action = ACTION_START_RECORDING
            }
            context.startService(recordingIntent)
        }

        /**
         * Stop audio recording service
         */
        fun stopMediaRecorderService(context: Context) {
            val stopRecordingIntent = Intent(context, MediaRecorderService::class.java).apply {
                action = ACTION_STOP_RECORDING
            }
            context.startService(stopRecordingIntent)
        }
    }
}