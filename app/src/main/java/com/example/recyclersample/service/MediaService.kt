package com.example.recyclersample.service

import android.annotation.SuppressLint
import android.app.*
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.IBinder
import android.provider.MediaStore
import android.provider.Settings
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.recyclersample.R
import com.example.recyclersample.app.MyApplication
import com.example.recyclersample.data.Song
import com.example.recyclersample.flowerList.FlowersListActivity

class MediaService : Service() {
    // Tạo object MediaPlayer
    private lateinit var player: MediaPlayer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val bundle = intent?.extras

//        if (bundle != null) {
//            val song: Song = bundle.get("object song") as Song
//            playMusic(song)
//            sendNotification(song)
//        }

        player = MediaPlayer.create(this,  Settings.System.DEFAULT_RINGTONE_URI)
        player.isLooping = true
        player.start()
        // Return status of program
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(song: Song) {
        val intent = Intent(this, FlowersListActivity::class.java)
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, song.image)
        val remoteView = RemoteViews(packageName, R.layout.notification)
        remoteView.setTextViewText(R.id.tv_title, song.title)
        remoteView.setTextViewText(R.id.tv_singer, song.singer)
        remoteView.setImageViewBitmap(R.id.img_song, bitmap)
        remoteView.setImageViewResource(R.id.btn_play, R.drawable.ic_vector_play_24dp)

        val notification: Notification =
            NotificationCompat.Builder(applicationContext, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_vector_close)
                .setContentIntent(pendingIntent)
                .setCustomContentView(remoteView)
                .setSound(null)
                .build()

        startForeground(1, notification)
    }

    private fun playMusic(song : Song) {
        player = MediaPlayer.create(applicationContext, song.resource)
        player.start()
    }
}