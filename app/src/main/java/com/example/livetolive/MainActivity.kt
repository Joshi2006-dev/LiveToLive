package com.example.livetolive

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navegacion: BottomNavigationView

    private var seleccion = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.home -> {
                supportFragmentManager.commit {
                    replace<HomeFragment>(R.id.Frame)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                true
            }

            R.id.achievement -> {
                supportFragmentManager.commit {
                    replace<AchievementFragment>(R.id.Frame)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                true
            }

            R.id.group -> {
                true
            }

            R.id.profile -> {
                supportFragmentManager.commit {
                    replace<ProfileFragment>(R.id.Frame)
                    setReorderingAllowed(true)
                    addToBackStack("replacement")
                }
                true
            }

            else -> false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "alarm_channel",
                "Alarma",
                NotificationManager.IMPORTANCE_HIGH
            )
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)

            val notif = NotificationCompat.Builder(this, "alarm_channel")
                .setContentTitle("Prueba de notificación")
                .setContentText("Activando canal de alarma")
                .setSmallIcon(R.drawable.ic_alarm)
                .build()

            val nm2 = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm2.notify(999, notif)
        }




        // permiso para las alarmas jodidas estas  (Android 12+)
        solicitarPermisoAlarma()

        navegacion = findViewById(R.id.NaviMenu)
        navegacion.setOnNavigationItemSelectedListener(seleccion)

        supportFragmentManager.commit {
            replace<HomeFragment>(R.id.Frame)
            setReorderingAllowed(true)
            addToBackStack("replacement")
        }
        val prefs = getSharedPreferences("alarm", MODE_PRIVATE)
        prefs.edit().putBoolean("alarma_activa", false).apply()

    }

    // y este es el permiso para que suenen en el fon  12+
    private fun solicitarPermisoAlarma() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(AlarmManager::class.java)

            // Si la app NO tiene permiso → se abre la pantalla para concederlo
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                startActivity(intent)
            }
        }
    }
}