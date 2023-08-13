package com.example.respolhpl.paging

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
private fun LoadMoreRefreshableErrorPreview() {
    MaterialTheme {
        LazyColumn {
            item {
                LoadMoreRefreshableError {}
            }
        }
    }
}

@Composable
fun LazyItemScope.LoadMoreRefreshableError(
    modifier: Modifier = Modifier,
    onRefreshClick: () -> Unit
) {
    RefreshableError(
        modifier = modifier,
        showButtonAtBottom = false,
        onRefreshClick = onRefreshClick,
    )
}