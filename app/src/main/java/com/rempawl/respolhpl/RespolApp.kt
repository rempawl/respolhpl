package com.rempawl.respolhpl

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


//todo order
//todo cart select

//todo cart summary
//todo shipping

// todo tests
// todo ui tests
// todo category filters on home screen
// todo feature modules

@HiltAndroidApp
class RespolApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setUpTimber()
    }

    private fun setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}