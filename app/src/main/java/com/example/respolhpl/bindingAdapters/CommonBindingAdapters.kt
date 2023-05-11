package com.example.respolhpl.bindingAdapters

import android.view.View

object CommonBindingAdapters {
    @JvmStatic
    fun View.showWhen(shouldShow: Boolean) {
        visibility = if (shouldShow) View.VISIBLE else View.GONE
    }


}
