package com.rempawl.respolhpl.productDetails.currentPageState

import kotlinx.coroutines.flow.StateFlow

interface ViewPagerPageManager {
    val currentPage: StateFlow<Int>
    fun saveCurrentPage(page: Int)
}
