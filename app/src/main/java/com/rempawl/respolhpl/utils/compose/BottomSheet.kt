package com.rempawl.respolhpl.utils.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <T> BottomSheetPicker(
    items: List<T>,
    title: String,
    onDismiss: () -> Unit = {},
    onItemClick: (T) -> Unit = {},
    itemView: @Composable (T) -> Unit = {}
) {
    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        modifier = Modifier.fillMaxWidth(),
        containerColor = Color.White
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth()
        )
        HeightSpacer()
        items.forEach {
            Button(
                onClick = { onItemClick(it) },
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.textButtonColors(),
            ) {
                itemView(it)
            }
        }
        HeightSpacer()
    }
}