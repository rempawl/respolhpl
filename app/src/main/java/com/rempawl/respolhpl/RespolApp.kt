package com.rempawl.respolhpl

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

// todo order
// todo cart delete item
// todo compose theme
// todo changing product quantity in cart
// todo shipping
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
