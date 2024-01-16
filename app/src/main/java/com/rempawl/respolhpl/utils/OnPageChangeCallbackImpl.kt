package com.rempawl.respolhpl.utils

import androidx.viewpager2.widget.ViewPager2
import com.rempawl.respolhpl.productDetails.currentPageState.ViewPagerPageManager

class OnPageChangeCallbackImpl(private val viewPagerPageManager: ViewPagerPageManager) :
    ViewPager2.OnPageChangeCallback() {

    override fun onPageSelected(position: Int) {
        super.onPageSelected(position)
        viewPagerPageManager.saveCurrentPage(position)
    }
}
