package com.rempawl.respolhpl.list.paging

import com.rempawl.respolhpl.utils.AppError
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.extensions.onError
import com.rempawl.respolhpl.utils.extensions.onSuccess
import com.rempawl.respolhpl.utils.extensions.refreshWhen
import com.rempawl.respolhpl.utils.extensions.throttleFirst
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class PagingManager<Item>(
    private val config: PagingConfig,
    scope: CoroutineScope,
    loadMoreTrigger: Flow<Unit>,
    private val dataSource: (PagingParam) -> Flow<EitherResult<List<Item>>>,
    private val idProducer: (Item) -> Any
) {

    private val _pagingData = MutableStateFlow(PagingData<Item>())
    val pagingData = _pagingData.asStateFlow()
        .onSubscription { initialize.emit(Unit) }

    private val initialize = MutableSharedFlow<Unit>()
    private val retry = MutableSharedFlow<Unit>()

    enum class LoadMorePhase {
        Init, LoadingMore
    }

    init {
        val init = initialize
            .map { LoadMorePhase.Init }

        val loadMore = loadMoreTrigger
            .filter { _pagingData.value.loadState !is LoadState.Loading }
            .throttleFirst(10) // to avoid multiple request for the same page
            .map { LoadMorePhase.LoadingMore }

        merge(init, loadMore)
            .flatMapMerge { phase ->
                loadItems(phase)
                    .refreshWhen(retry)
            }
            .launchIn(scope)
    }

    suspend fun retry() {
        retry.emit(Unit)
    }

    suspend fun refresh() {
        initialize.emit(Unit)
    }

    private fun loadItems(phase: LoadMorePhase): Flow<EitherResult<List<Item>>> {
        return flow {
            updateLoadingState(phase)
            emitAll(
                dataSource(PagingParam(currentPageToLoad(), getPageSize(phase)))
                    .onSuccess { newItems ->
                        _pagingData.update {
                            it.copy(
                                items = getListWithoutDuplicates(it.items, newItems),
                                loadState = LoadState.Success
                            )
                        }
                    }
                    .onError { error ->
                        _pagingData.update { it.copy(loadState = getErrorState(phase, error)) }
                    }
            )
        }
    }

    // todo pass param into result, to add support for cache and fresh
    private fun getListWithoutDuplicates(
        items: List<Item>,
        newItems: List<Item>
    ) = (items + newItems)
        .asReversed() // reversing so latest items are left in list if they are duplicates
        .distinctBy(idProducer)
        .asReversed()

    private suspend fun updateLoadingState(phase: LoadMorePhase) {
        when (phase) {
            LoadMorePhase.Init -> {
                _pagingData.emit(
                    PagingData(
                        emptyList(),
                        LoadState.Loading.InitialLoading
                    )
                )
            }

            LoadMorePhase.LoadingMore -> {
                _pagingData.emit(_pagingData.value.copy(loadState = LoadState.Loading.LoadingMore))
            }
        }
    }

    private fun currentPageToLoad(): Int {
        val itemsCount = _pagingData.value.items.size
        return if (itemsCount == 0) {
            1
        } else {
            with(config) {
                val nextPages = (itemsCount - prefetchSize).takeIf { it > 0 } ?: 0
                val pages = nextPages / perPage
                prefetchSize.div(perPage).plus(pages).plus(1)
            }
        }
    }

    private fun getErrorState(phase: LoadMorePhase, error: AppError) =
        when (phase) {
            LoadMorePhase.Init -> LoadState.Error.InitError(error)
            LoadMorePhase.LoadingMore -> LoadState.Error.LoadMoreError(error)
        }

    private fun getPageSize(phase: LoadMorePhase) =
        when (phase) {
            LoadMorePhase.Init -> config.prefetchSize
            LoadMorePhase.LoadingMore -> config.perPage
        }
}
