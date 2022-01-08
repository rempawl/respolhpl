package com.example.respolhpl.productDetails.currentPageState

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow

interface CurrentViewPagerPage {
    val currentPage: StateFlow<Int>
    fun saveCurrentPage(page: Int)
}