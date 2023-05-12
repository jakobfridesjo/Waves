package com.example.myapplication.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.os.PowerManager
import java.io.IOException

class MediaPlayerService : Service(), MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private var mMediaPlayer: MediaPlayer? = null
    private var mStreamUrl: String? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_PLAY -> {
                // Get Stream url
                mStreamUrl = intent.getStringExtra(EXTRA_STREAM_URL)
                if (mStreamUrl == null) {
                    return START_NOT_STICKY
                }

                // Stop and release the media player
                mMediaPlayer?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    release()
                }

                // Set audio attributes
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .build()

                // Build media player, acquire partial wake lock to keep cpu alive
                mMediaPlayer = MediaPlayer().apply {
                    setWakeMode(applicationContext, PowerManager.PARTIAL_WAKE_LOCK)
                    setAudioAttributes(audioAttributes)
                    setDataSource(mStreamUrl)
                    setOnPreparedListener(this@MediaPlayerService)
                    setOnErrorListener(this@MediaPlayerService)
                    prepareAsync() // prepare async to not block main thread
                }

                // Timeout if the player is not ready within 10 seconds
                // TODO TIMEOUT IF NOT PLAYING WITHIN 10 SECONDS
                /*
                val handlerThread = HandlerThread("MediaServiceTimeout")
                handlerThread.start()
                val handler = Handler(handlerThread.looper)
                handler.postDelayed({
                    if (mMediaPlayer?.isPlaying == true) {
                        stopSelf()
                    }
                    handlerThread.quitSafely()
                }, 10000)
                */
                return START_STICKY
            }

            ACTION_STOP -> {
                // Stop and release media player
                mMediaPlayer?.apply {
                    if (isPlaying) {
                        stop()
                    }
                    release()
                }
                // Stop the service and wake locks
                stopSelf()
                mMediaPlayer = null
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
        mMediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        super.onDestroy()
    }

    companion object {
        private var mMediaPlayerIntent: Intent? = null

        const val ACTION_PLAY: String = "com.example.myapplication.action.PLAY"
        const val ACTION_STOP: String = "com.example.myapplication.action.STOP"
        const val EXTRA_STREAM_URL: String = "EXTRA_STREAM_URL"

        /**
         * Setup new media player service
         */
        fun startMediaService(context: Context, streamUrl: String) {
            // If the service is running, stop it before starting a the one
            if (mMediaPlayerIntent?.action == ACTION_PLAY) {
                stopMediaService(context)
                mMediaPlayerIntent = null
            }

            // Setup a service to prepare a media player asynchronously and allow background play
            mMediaPlayerIntent = Intent(context, MediaPlayerService::class.java).apply {
                action = ACTION_PLAY
                putExtra(EXTRA_STREAM_URL, streamUrl)
            }
            context.startService(mMediaPlayerIntent)
        }

        /**
         * Stop media player service
         */
        fun stopMediaService(context: Context) {
            mMediaPlayerIntent?.let {
                context.stopService(Intent(context, MediaPlayerService::class.java).apply {
                    action = it.action
                })
            }
        }
    }

}
