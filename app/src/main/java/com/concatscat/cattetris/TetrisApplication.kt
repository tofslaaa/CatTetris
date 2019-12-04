package com.concatscat.cattetris

import android.app.Application
import timber.log.Timber

class TetrisApplication : Application() {
    override fun onCreate() {
        super.onCreate()
         Timber.plant(Timber.DebugTree())
    }
}