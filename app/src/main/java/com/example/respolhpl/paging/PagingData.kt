package com.example.respolhpl.paging


data class PagingData<T>(
    val items: List<T> = emptyList(),
    val loadState: LoadState = LoadState.Loading.InitialLoading
) {
    val isPlaceholderVisible: Boolean = loadState is LoadState.Success && items.isEmpty()
}
