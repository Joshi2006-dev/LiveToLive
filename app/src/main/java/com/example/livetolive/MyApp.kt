package com.example.livetolive

import android.app.Application

class MyApp: Application() {
    override fun onCreate() {
        super.onCreate()
        sharedPreferencesApp.init(this  )
    }
}