package com.example.respolhpl

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


//todo order
//todo favs
//todo cart select

//todo cart summary
//todo shipping

// todo tests
// todo ui tests

// todo splash

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
