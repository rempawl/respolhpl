package com.rempawl.respolhpl.utils.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.rempawl.respolhpl.R
import com.rempawl.respolhpl.utils.log


@Composable
fun QuantityCounter(
    onMinusClick: () -> Unit,
    onPlusClick: () -> Unit,
    quantityText: String,
    onQuantityChange: (String) -> Unit,
    isPlusBtnEnabled: Boolean,
    isMinusBtnEnabled: Boolean,
) {
    SelectionContainer {
        val keyboardOptions = remember {
            KeyboardOptions(keyboardType = KeyboardType.Number)
        }
        OutlinedTextField(
            singleLine = true,
            keyboardOptions = keyboardOptions,
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            shape = CircleShape,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .clearFocusOnClick(),
                    onClick = onMinusClick,
                    enabled = isMinusBtnEnabled
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_minus),
                        contentDescription = "minus"
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    modifier = Modifier
                        .size(48.dp)
                        .clearFocusOnClick(),
                    onClick = onPlusClick,
                    enabled = isPlusBtnEnabled
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_plus),
                        contentDescription = "plus"
                    )
                }
            },
            value = quantityText,
            onValueChange = {
                log { "kruci text = $it" }
                it.takeIf { it.isDigitsOnly() }?.let(onQuantityChange)
            },
            modifier = Modifier
                .clearFocusOnKeyboardDismiss()
                .width(160.dp),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun QuantityIndicatorPreview() {
//    QuantityCounter({}, {}, "1") {}
}

@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
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


fun Modifier.clearFocusOnClick(): Modifier = composed {
    val focusManager = LocalFocusManager.current
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = null
    ) { focusManager.clearFocus() }
}