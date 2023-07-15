package com.example.respolhpl.productDetails.currentPageState

import com.example.respolhpl.utils.BaseCoroutineTest
import org.junit.Before
import org.junit.Test


class ViewPagerPageManagerImplTest : BaseCoroutineTest() {
    lateinit var currentPageState: ViewPagerPageManagerImpl

    @Before
    fun setup() {
        currentPageState = ViewPagerPageManagerImpl()
    }

    @Test
    fun init() {
    }

    @Test
    fun saveStateTo5Then3() {
//        val res = currentPageState.currentPage.getOrAwaitValue()
//        assertThat(res, `is`(0))

        currentPageState.saveCurrentPage(5)
//        val five = currentPageState.currentPage.getOrAwaitValue()
//        assertThat(five, `is`(5))

        currentPageState.saveCurrentPage(3)

//        val three = currentPageState.currentPage.getOrAwaitValue()
//        assertThat(three, `is`(3))
    }
}
