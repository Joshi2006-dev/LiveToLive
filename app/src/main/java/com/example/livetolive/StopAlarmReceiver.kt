package com.example.livetolive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {

        // este detiene la alarma fea
        context.stopService(Intent(context, AlarmService::class.java))

        // LIBERAR LA BANDERA DE ALARMA ACTIVA
        val prefs = context.getSharedPreferences("alarm", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("alarma_activa", false).apply()
    }
}
