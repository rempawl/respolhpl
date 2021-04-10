package com.example.respolhpl.productDetails.currentPageState

import androidx.lifecycle.LiveData

interface CurrentPageState {
    val currentPage: LiveData<Int>
    fun saveCurrentPage(page: Int)
}