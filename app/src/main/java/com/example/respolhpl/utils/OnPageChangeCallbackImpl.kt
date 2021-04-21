package com.example.respolhpl.utils

import androidx.viewpager2.widget.ViewPager2
import com.example.respolhpl.productDetails.currentPageState.CurrentViewPagerPage

class OnPageChangeCallbackImpl(private val currentViewPagerPage: CurrentViewPagerPage) :
    ViewPager2.OnPageChangeCallback() {
    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        currentViewPagerPage.saveCurrentPage(position)
    }

}