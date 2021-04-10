package com.example.respolhpl.productDetails.currentPageState

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class CurrentPageStateImpl @Inject constructor() : CurrentPageState {

    //todo test
    private var _currentPage = MutableLiveData(0)
    override val currentPage: LiveData<Int>
        get() = _currentPage

    override fun saveCurrentPage(page: Int) {
        _currentPage.value = page
    }

}