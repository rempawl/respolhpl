package com.example.respolhpl.paging

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable

interface BaseListItem {
    val itemId: Any
}

open class UniqueListItem : BaseListItem {
    override val itemId: Any = this::class.java.simpleName
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

inline fun <T : BaseListItem> LazyListScope.listItemsIndexed(
    items: List<T>,
    crossinline itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) = itemsIndexed(
    items = items,
    key = { _, item -> item.itemId },
    contentType = { _, item -> item.javaClass }
) { index, item ->
    itemContent(index, item)
}

class UnknownListItemException(
    item: BaseListItem
) : IllegalArgumentException("Unknown list element: $item")
