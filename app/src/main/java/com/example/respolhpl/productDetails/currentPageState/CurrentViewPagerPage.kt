package com.example.respolhpl.productDetails.currentPageState

import androidx.lifecycle.LiveData

interface CurrentViewPagerPage {
    val currentPage: LiveData<Int>
    fun saveCurrentPage(page: Int)
}