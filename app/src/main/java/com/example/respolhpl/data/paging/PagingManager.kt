package com.example.respolhpl.data.paging

import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.EitherResult
import com.example.respolhpl.utils.extensions.onError
import com.example.respolhpl.utils.extensions.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class PagingManager<Item>(
    private val config: PagingConfig,
    scope: CoroutineScope,
    loadMoreTrigger: Flow<Unit>,
    private val dataSource: (PagingParam) -> Flow<EitherResult<List<Item>>>
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
            .throttleFirst(100)
            .map { LoadMorePhase.LoadingMore }

        merge(init, loadMore)
            .flatMapLatest { phase ->
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
                dataSource(PagingParam(loadedItemsSize(), getLimit(phase)))
                    .onSuccess { newItems ->
                        _pagingData.update {
                            it.copy(
                                items = it.items + newItems,
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

    private fun loadedItemsSize() = _pagingData.value.items.size

    private fun getErrorState(phase: LoadMorePhase, error: DefaultError) =
        when (phase) {
            LoadMorePhase.Init -> LoadState.Error.InitError(error)
            LoadMorePhase.LoadingMore -> LoadState.Error.LoadMoreError(error)
        }

    private fun getLimit(phase: LoadMorePhase) =
        when (phase) {
            LoadMorePhase.Init -> config.prefetchSize
            LoadMorePhase.LoadingMore -> config.limit
        }
}
/**
 *  Code taken from https://github.com/Kotlin/kotlinx.coroutines/issues/1446#issuecomment-625244176
 */
fun <T> Flow<T>.throttleFirst(windowDuration: Long): Flow<T> {
    var delayJob: Job = Job().apply { complete() }
    return onCompletion { delayJob.cancel() }.run {
        flow {
            coroutineScope {
                collect { value ->
                    if (!delayJob.isActive) {
                        emit(value)
                        delayJob = launch { delay(windowDuration) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.refreshWhen(refreshFlow: SharedFlow<Unit>): Flow<T> {
    return refreshFlow
        .onSubscription { emit(Unit) }
        .flatMapLatest {
            this
        }
}
