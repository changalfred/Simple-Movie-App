package com.example.simplemovieapp

import android.app.Application
import timber.log.Timber

class MovieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}