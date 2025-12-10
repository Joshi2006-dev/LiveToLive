package com.example.livetolive

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi

class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        val i = Intent(context, AlarmService::class.java)

        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startForegroundService(i)
    }
}