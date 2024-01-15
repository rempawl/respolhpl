package com.rempawl.respolhpl.list.paging

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun LoadMoreManager(
    listState: LazyListState,
    onLoadMore: () -> Unit,
    threshold: Int = PagingConstants.DEFAULT_THRESHOLD
) {
    LaunchedEffect(Unit) {
        snapshotFlow { listState.lastVisibleItemIndex }
            .distinctUntilChanged()
            .map { listState }
            .filter { it.layoutInfo.totalItemsCount > threshold }
            .filter { it.layoutInfo.totalItemsCount - it.lastVisibleItemIndex <= threshold }
            .collect { onLoadMore() }
    }
}

val LazyListState.lastVisibleItemIndex
    get() = firstVisibleItemIndex + layoutInfo.visibleItemsInfo.lastIndex

object PagingConstants {
    const val DEFAULT_THRESHOLD = 10
}