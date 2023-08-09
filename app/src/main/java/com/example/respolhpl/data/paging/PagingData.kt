package com.example.respolhpl.data.paging


data class PagingData<T>(
    val items: List<T> = emptyList(),
    val loadState: LoadState = LoadState.Loading.InitialLoading
) {
    val isPlaceholderVisible: Boolean = loadState is LoadState.Success && items.isEmpty()
}
