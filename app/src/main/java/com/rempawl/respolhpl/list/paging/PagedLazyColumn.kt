package com.rempawl.respolhpl.list.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
            .then(modifier),
        state = listState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing)
    ) {
        content()
    }
}