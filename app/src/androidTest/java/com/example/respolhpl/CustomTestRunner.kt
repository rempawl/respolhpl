package com.example.respolhpl

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.example.respolhpl.utils.CustomHiltTestApplication
import dagger.hilt.android.testing.CustomTestApplication
import dagger.hilt.android.testing.HiltTestApplication

class CustomTestRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader?, name: String?, context: Context?): Application {
        return super.newApplication(cl, CustomHiltTestApplication::class.java.name, context)
    }
}