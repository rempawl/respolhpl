package com.example.respolhpl.productDetails.currentPageState

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.respolhpl.getOrAwaitValue
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ViewPagerPageManagerImplTest {
    @get:Rule
    val taskExecutor = InstantTaskExecutorRule()
    lateinit var currentPageState: ViewPagerPageManagerImpl

    @Before
    fun setup() {
        currentPageState = ViewPagerPageManagerImpl()
    }

    @Test
    fun init() {
        val res = currentPageState.currentPage.getOrAwaitValue()
        assertThat(res, `is`(0))
    }

    @Test
    fun saveStateTo5Then3() {
        val res = currentPageState.currentPage.getOrAwaitValue()
        assertThat(res, `is`(0))

        currentPageState.saveCurrentPage(5)
        val five = currentPageState.currentPage.getOrAwaitValue()
        assertThat(five, `is`(5))

        currentPageState.saveCurrentPage(3)

        val three = currentPageState.currentPage.getOrAwaitValue()
        assertThat(three, `is`(3))


    }
}
