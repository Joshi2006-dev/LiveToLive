package com.example.livetolive

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class AlarmService : Service() {

    private lateinit var player: MediaPlayer
    private val CHANNEL_ID = "alarm_channel"

    override fun onCreate() {
        super.onCreate()

        // Reproducir sonido
        player = MediaPlayer.create(this, R.raw.alarm_sound)
        player.isLooping = true
        player.start()

        // Crear canal de notificación
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Alarma",
                NotificationManager.IMPORTANCE_HIGH
            )
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }

        // PendingIntent del botón DETENER
        val stopIntent = Intent(this, StopAlarmReceiver::class.java)
        val stopPendingIntent = PendingIntent.getBroadcast(
            this,
            1,
            stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Notificación CORREGIDA para mostrar siempre el botón DETENER
        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarma")
            .setContentText("Despierta")
            .setSmallIcon(R.drawable.ic_alarm) // Tu ícono sí sirve
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH) // NECESARIO EN SAMSUNG
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) // Muestra acciones
            .addAction(0, "Detener", stopPendingIntent) // EL BOTÓN DETENER
            .build()

        startForeground(1, notif)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::player.isInitialized) {
            player.stop()
            player.release()
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
