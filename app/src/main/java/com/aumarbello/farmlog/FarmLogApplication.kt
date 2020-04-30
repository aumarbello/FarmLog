package com.aumarbello.farmlog

import androidx.multidex.MultiDexApplication
import timber.log.Timber

class FarmLogApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}