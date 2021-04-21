package com.example.respolhpl.bindingAdapters

import android.view.View
import androidx.databinding.BindingAdapter

object CommonBindingAdapters {
    @JvmStatic
    @BindingAdapter("showWhen")
    fun View.showWhen(shouldShow: Boolean) {
        visibility = if (shouldShow) View.VISIBLE else View.GONE
    }


}