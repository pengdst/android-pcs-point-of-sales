package io.github.pengdst.salescashier.ui

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import io.github.pengdst.salescashier.BuildConfig
import timber.log.Timber

@HiltAndroidApp
class SalesApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}