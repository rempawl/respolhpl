package com.example.respolhpl.productDetails.currentPageState

import kotlinx.coroutines.flow.StateFlow

interface CurrentViewPagerPage {
    val currentPage: StateFlow<Int>
    fun saveCurrentPage(page: Int)
}
