package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder

class MediaPlayerService: Service(), MediaPlayer.OnPreparedListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mStreamUrl: String? = "https://boxradio-edge-00.streamafrica.net/jpopchill"

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        when(intent.action) {
            ACTION_PLAY -> {
                // Set audio attributes
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()

                // Build media player
                mMediaPlayer = MediaPlayer()
                mMediaPlayer!!.setAudioAttributes(audioAttributes)
                mMediaPlayer!!.setDataSource(mStreamUrl)

                // Wait for it to be ready asynchronously
                mMediaPlayer?.apply {
                    setOnPreparedListener(this@MediaPlayerService)
                    prepareAsync() // prepare async to not block main thread
                }
            }
            //TODO Make it possible to change stream TODO
            ACTION_CHANGE_STREAM_URL -> {
                // Change stream URL using intent
                mStreamUrl = intent.getStringExtra(EXTRA_STREAM_URL)

                // Stop and release media player
                if (mMediaPlayer?.isPlaying == true) {
                    mMediaPlayer?.stop()
                    mMediaPlayer?.release()
                    mMediaPlayer = null
                }

                // Restart the service
                val restartIntent = Intent(this, MediaPlayerService::class.java)
                startService(restartIntent)
            }
        }
        return START_STICKY
    }

    /**
     * I don't know what to do here, so return null for now
     */
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    /**
     * Start playing directly when ready
     */
    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }

    /**
     * Stop playing properly
     */
    override fun onDestroy() {
        if (mMediaPlayer?.isPlaying() == true) {
            mMediaPlayer?.stop();
        }
        mMediaPlayer?.release();
        mMediaPlayer?.release();
    }

    companion object {
        const val ACTION_PLAY: String = "com.example.myapplication.action.PLAY"
        const val ACTION_CHANGE_STREAM_URL: String = "com.example.myapplication.action.CHANGE_STREAM_URL"
        const val EXTRA_STREAM_URL = "com.example.myapplication.action.STREAM_URL"
    }
}
