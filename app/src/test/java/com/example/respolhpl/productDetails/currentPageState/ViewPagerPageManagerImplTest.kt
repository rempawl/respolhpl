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

    // todo
    @Test
    fun init() {
    }

    @Test
    fun saveStateTo5Then3() {

        currentPageState.saveCurrentPage(5)

        currentPageState.saveCurrentPage(3)

    }
}
