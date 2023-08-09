package com.example.respolhpl.utils.extensions

import android.view.View
import android.widget.EditText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun EditText.setTextIfDifferent(textToSet: String) {
    if (this.text.toString() != textToSet) {
        setText(textToSet)
    }
}