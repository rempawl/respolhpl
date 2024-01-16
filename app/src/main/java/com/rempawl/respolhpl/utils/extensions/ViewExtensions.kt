package com.rempawl.respolhpl.utils.extensions

import android.content.res.Resources
import android.util.TypedValue
import android.widget.EditText

fun EditText.setTextIfDifferent(newText: String) {
    if (this.text.toString() != newText) {
        setText(newText)
    }
}

fun Resources.dpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, displayMetrics)

fun Int.dpToPx(resources: Resources): Int = resources.dpToPx().times(this).toInt()
