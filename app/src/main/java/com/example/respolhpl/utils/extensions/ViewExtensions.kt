package com.example.respolhpl.utils.extensions

import android.content.res.Resources
import android.util.TypedValue
import android.widget.EditText

fun EditText.setTextIfDifferent(textToSet: String) {
    if (this.text.toString() != textToSet) {
        setText(textToSet)
    }
}

fun Resources.dpToPx() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, displayMetrics)

fun Int.dpToPx(resources: Resources): Int = resources.dpToPx().times(this).toInt()
