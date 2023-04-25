package com.example.myapplication

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import java.io.IOException

class MediaPlayerService: Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mStreamUrl: String? = null
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        when(intent.action) {
            ACTION_PLAY -> {
                // Get Stream url
                mStreamUrl = intent.getStringExtra(EXTRA_STREAM_URL)
                if (mStreamUrl == null) {
                    return START_NOT_STICKY
                }

                // Stop and release the media player
                if (mMediaPlayer?.isPlaying == true) {
                    mMediaPlayer?.stop()
                }

                // Set audio attributes
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()

                // Build media player, acquire partial wake lock to keep cpu alive
                mMediaPlayer = MediaPlayer().apply {
                    setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                }
                mMediaPlayer!!.setAudioAttributes(audioAttributes)
                mMediaPlayer!!.setDataSource(mStreamUrl)

                // Wait for it to be ready asynchronously
                mMediaPlayer?.apply {
                    setOnPreparedListener(this@MediaPlayerService)
                    setOnErrorListener(this@MediaPlayerService)
                    prepareAsync() // prepare async to not block main thread
                }

                return START_STICKY
            }

            ACTION_STOP -> {
                // Stop and release media player
                if (mMediaPlayer?.isPlaying == true) {
                    mMediaPlayer?.stop()
                    mMediaPlayer?.release()
                    mMediaPlayer = null
                }
                // Stop the service and wake locks
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
     * Start playing directly when ready
     */
    override fun onPrepared(mediaPlayer: MediaPlayer) {
        mediaPlayer.start()
    }

    /**
     * Try to restart on error and stop service if restart didn't work
     */
    override fun onError(mediaPlayer: MediaPlayer?, what: Int, extra: Int): Boolean {
        try {
            mediaPlayer?.setDataSource(mStreamUrl)
            mediaPlayer?.prepareAsync()
        } catch (e: IOException) {
            // If restarting doesn't work, stop
            stopSelf()
        }
        return true
    }

    /**
     * Stop playing properly
     */
    override fun onDestroy() {
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop();
        }
        mMediaPlayer?.release();
        mMediaPlayer = null
    }

    companion object {
        const val ACTION_PLAY: String = "com.example.myapplication.action.PLAY"
        const val ACTION_STOP: String = "com.example.myapplication.action.STOP"
        const val EXTRA_STREAM_URL: String = "EXTRA_STREAM_URL"
    }
}
