package com.example.myapplication

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaPlayer = MediaPlayer()
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .build()

        mediaPlayer.setAudioAttributes(audioAttributes)
        mediaPlayer.setDataSource("https://sh.onweb.gr:7129/")

        try {
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: Exception) {
            Log.e("MainActivity", "Error playing radio station", e)
        }
    }
}
