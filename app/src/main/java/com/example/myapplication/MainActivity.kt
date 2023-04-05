package com.example.myapplication

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* Get binding of the main activity*/
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /* Bottom nav bar */
        //val navView: BottomNavigationView = binding.navView
        //val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //val appBarConfiguration = AppBarConfiguration(setOf(
        //    R.id.navigation_notes, R.id.navigation_navigation, R.id.navigation_utilities))
        //setupActionBarWithNavController(navController, appBarConfiguration)
        //navView.setupWithNavController(navController)

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
