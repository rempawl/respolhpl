package com.example.respolhpl.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable

interface BaseListItem {
    val itemId: Any
}

inline fun <T : BaseListItem> LazyListScope.listItems(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(item: T) -> Unit
) = items(
    items = items,
    key = { item -> item.itemId },
    contentType = { item -> item.javaClass }
) { item ->
    itemContent(item)
}