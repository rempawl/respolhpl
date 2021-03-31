package com.example.respolhpl.productDetails

import com.example.respolhpl.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ProductDetailsFragmentTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject

    @Before
    fun setup() {
        val scenario = launchFragmentInHiltContainer<ProductDetailsFragment> { }
    }

    @Test
    fun launch() {
        println("test")
    }
}
