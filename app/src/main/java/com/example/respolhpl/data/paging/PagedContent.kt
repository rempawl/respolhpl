package com.example.respolhpl.data.paging

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.respolhpl.R
import com.example.respolhpl.utils.extensions.DefaultError

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
fun LazyItemScope.ListItemRefreshableError(
    modifier: Modifier = Modifier,
    isLoadingVisible: Boolean,
    onRefreshClick: () -> Unit
) {
    RefreshableError(
        modifier = modifier.fillParentMaxHeight(0.8f), // 0.8 Makes it more centered
        isLoadingVisible = isLoadingVisible,
        showButtonAtBottom = false,
        onRefreshClick = onRefreshClick,
    )
}

@Composable
fun RefreshableError(
    modifier: Modifier = Modifier.fillMaxSize(),
    showButtonAtBottom: Boolean = true,
    isLoadingVisible: Boolean,
    onRefreshClick: () -> Unit
) {
    Column(
        Modifier
            .padding(horizontal = 30.dp, vertical = 34.dp)
            .then(modifier),
        verticalArrangement = if (showButtonAtBottom) {
            Arrangement.SpaceBetween
        } else {
            Arrangement.Center
        }
    ) {
        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.an_error_occurred),
//                style = MaterialTheme.typography.h1,
//                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.error_no_internet_connection_refresh),
//                style = MaterialTheme.typography.body1,
//                color = MaterialTheme.colors.onSurfaceMedium()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            onClick = onRefreshClick
        ) {
            Text(text = stringResource(R.string.retry))
//            isLoadingVisible = isLoadingVisible,
        }
    }
}

internal object PagerPreviewDataCreator {
    data class FakeItem(val id: String, override val itemId: Any = id) : BaseListItem

    private val items = buildList<FakeItem> {
        repeat(5) {
            add(FakeItem("id $it"))
        }
    }
    val loadingData = PagingData<FakeItem>(emptyList(), LoadState.Loading.InitialLoading)
    val successData = PagingData<FakeItem>(items, LoadState.Success)
    val loadMoreData = PagingData(items, LoadState.Loading.LoadingMore)
    val loadMoreErrorData = PagingData(items, LoadState.Error.LoadMoreError(DefaultError()))
    val errorData = PagingData<FakeItem>(emptyList(), LoadState.Error.InitError(DefaultError()))
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
        data.loadState is LoadState.Error.InitError -> item {
            ListItemRefreshableError(
                isLoadingVisible = false,
                onRefreshClick = retry
            )
        }

        data.loadState is LoadState.Loading.InitialLoading -> item {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        else -> listItems(data.items) { itemView(it) }
    }

    when (data.loadState) {
        is LoadState.Error.LoadMoreError -> item {
            LoadMoreRefreshableError(onRefreshClick = retry)
        }

        LoadState.Loading.LoadingMore -> item {
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


@Composable
fun LazyItemScope.LoadMoreRefreshableError(
    modifier: Modifier = Modifier,
    onRefreshClick: () -> Unit
) {
    RefreshableError(
        modifier = modifier,
        isLoadingVisible = false,
        showButtonAtBottom = false,
        onRefreshClick = onRefreshClick,
    )
}