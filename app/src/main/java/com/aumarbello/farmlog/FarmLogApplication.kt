package com.aumarbello.farmlog

import androidx.multidex.MultiDexApplication
import com.mapbox.mapboxsdk.Mapbox
import timber.log.Timber

class FarmLogApplication: MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
        try {
            Mapbox.getInstance(applicationContext, getString(R.string.mapbox_access_token))
        }catch (ex: UnsatisfiedLinkError) {}
    }
}