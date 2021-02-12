package com.example.respolhpl

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Rule

@HiltAndroidTest
class MainActivityTest{
    @get:Rule
    val rule = HiltAndroidRule(this)
}