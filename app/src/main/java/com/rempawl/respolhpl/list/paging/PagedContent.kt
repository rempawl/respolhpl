package com.rempawl.respolhpl.list.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rempawl.respolhpl.list.BaseListItem
import com.rempawl.respolhpl.list.listItems
import com.rempawl.respolhpl.list.paging.LoadState.*
import com.rempawl.respolhpl.utils.AppError

internal object PagerPreviewDataCreator {
    data class FakeItem(val id: String, override val itemId: Any = id) : BaseListItem

    private val items = buildList {
        repeat(5) {
            add(FakeItem("id $it"))
        }
    }
    val loadingData = PagingData<FakeItem>(emptyList(), Loading.InitialLoading)
    val successData = PagingData(items, Success)
    val loadMoreData = PagingData(items, Loading.LoadingMore)
    val loadMoreErrorData = PagingData(items, Error.LoadMoreError(AppError()))
    val errorData = PagingData<FakeItem>(emptyList(), Error.InitError(AppError()))
}

@Preview(showBackground = true)
@Composable
private fun LoadingPreview() {
    MaterialTheme {
        LazyColumn(Modifier.fillMaxSize()) {
            pagedContent(PagerPreviewDataCreator.loadingData, {}, {}) {
                PreviewListItem(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SuccessPreview() {
    MaterialTheme {
        LazyColumn(Modifier.fillMaxSize()) {
            pagedContent(PagerPreviewDataCreator.successData, {}, {}) {
                PreviewListItem(it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadMorePreview() {
    MaterialTheme {
        LazyColumn(Modifier.fillMaxSize()) {
            pagedContent(PagerPreviewDataCreator.loadMoreData, {}, {}) {
                PreviewListItem(item = it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadMoreErrorPreview() {
    MaterialTheme {
        LazyColumn(Modifier.fillMaxSize()) {
            pagedContent(PagerPreviewDataCreator.loadMoreErrorData, {}, {}) {
                PreviewListItem(item = it)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorPreview() {
    MaterialTheme {
        LazyColumn(Modifier.fillMaxSize()) {
            pagedContent(PagerPreviewDataCreator.errorData, {}, {}) {
                PreviewListItem(item = it)
            }
        }
    }
}

@Composable
private fun PreviewListItem(item: PagerPreviewDataCreator.FakeItem) {
    Text(
        item.id,
        Modifier
            .padding(16.dp)
            .fillMaxWidth()
    )
}

fun <T : BaseListItem> LazyListScope.pagedContent(
    data: PagingData<T>,
    retry: () -> Unit,
    emptyPlaceholder: @Composable () -> Unit,
    itemView: @Composable (T) -> Unit,
) {

    when {
        data.isPlaceholderVisible -> item { emptyPlaceholder() }
        data.loadState is Error.InitError -> item {
            ListItemRefreshableError(
                isLoadingVisible = false,
                onRefreshClick = retry
            )
        }

        data.loadState is Loading.InitialLoading -> item {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        else -> listItems(data.items) { itemView(it) }
    }

    when (data.loadState) {
        is Error.LoadMoreError -> item {
            LoadMoreRefreshableError(onRefreshClick = retry)
        }

        Loading.LoadingMore -> item {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp),
                Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        else -> Unit // handled above
    }
}


