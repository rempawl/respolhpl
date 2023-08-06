package com.example.respolhpl.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PagedLazyColumn(
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = 16.dp,
    listState: LazyListState,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .then(modifier),
        state = listState,
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        content()
    }
}