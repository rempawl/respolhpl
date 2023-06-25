package com.example.respolhpl.productDetails.currentPageState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class ViewPagerPageManagerImpl @Inject constructor() : ViewPagerPageManager {

    private var _currentPage = MutableStateFlow(0)
    override val currentPage: StateFlow<Int>
        get() = _currentPage

    override fun saveCurrentPage(page: Int) {
        _currentPage.update { page }
    }

}
