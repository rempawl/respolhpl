package com.rempawl.respolhpl.productDetails.currentPageState

import com.rempawl.respolhpl.utils.BaseCoroutineTest
import org.junit.Before
import org.junit.Test


class ViewPagerPageManagerImplTest : BaseCoroutineTest() {
    lateinit var currentPageState: ViewPagerPageManagerImpl

    @Before
    fun setup() {
        currentPageState = ViewPagerPageManagerImpl()
    }

}
