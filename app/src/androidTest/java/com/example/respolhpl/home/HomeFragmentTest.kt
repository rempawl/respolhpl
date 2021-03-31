package com.example.respolhpl.home

import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.respolhpl.R
import com.example.respolhpl.mock
import com.example.respolhpl.utils.RecyclerViewMatcher
import com.example.respolhpl.utils.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeFragmentTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    private val navController = mock<NavController>()

    val recyclerViewMatcher
        get() = RecyclerViewMatcher(R.id.product_list)

    @Before
    fun setUp() {
        hiltRule.inject()
        launchFragmentInHiltContainer<HomeFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }
    }

    @Test
    fun test1(){
        println("tesssst")
    }
}