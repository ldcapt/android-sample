package com.example.recyclersample.service

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.Settings

class MediaService : Service() {
    // Tạo object MediaPlayer
    private lateinit var player : MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Tạo media player với default ringtone
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()

        // Return status of program
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

}