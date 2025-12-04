package com.example.livetolive


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    private lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()

        // sonido
        player = MediaPlayer.create(this, R.raw.alarm_sound)
        player.isLooping = true
        player.start()

        // canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "Alarma",
                NotificationManager.IMPORTANCE_HIGH
            )
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }

        // notificación
        val notif = NotificationCompat.Builder(this, "alarm_channel")
            .setContentTitle("Alarma")
            .setContentText("Despierta")
            .setSmallIcon(R.drawable.ic_alarm)
            .setAutoCancel(true)
            .build()

        startForeground(1, notif)
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
