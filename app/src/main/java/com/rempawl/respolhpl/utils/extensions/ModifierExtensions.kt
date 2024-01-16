package com.rempawl.respolhpl.utils.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.platform.LocalFocusManager


@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = this.composed {
    var isCurrentFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isCurrentFocused) {
        val isImeVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(isImeVisible) {
            when {
                isImeVisible -> keyboardAppearedSinceLastFocused = true
                keyboardAppearedSinceLastFocused -> focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isCurrentFocused != it.isFocused) {
            isCurrentFocused = it.isFocused
            if (isCurrentFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}


fun Modifier.clearFocusOnClick(): Modifier = this.composed {
    val focusManager = LocalFocusManager.current
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) { focusManager.clearFocus() }
}