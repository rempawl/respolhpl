package com.example.respolhpl.utils

import androidx.viewpager2.widget.ViewPager2
import com.example.respolhpl.productDetails.currentPageState.CurrentPageState

class OnPageChangeCallbackImpl(private val currentPageState: CurrentPageState) :
    ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        currentPageState.saveCurrentPage(position)
    }

}