package com.example.respolhpl.data.store

import arrow.core.left
import arrow.core.right
import com.dropbox.android.external.store4.*
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.EitherResult
import com.example.respolhpl.utils.extensions.runAsResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.toDuration
import kotlin.time.toDurationUnit

@ExperimentalCoroutinesApi
@ExperimentalTime
class ResponseStore<Key : Any, Response : Any, Output : Any>(
    private val request: suspend (Key) -> Response,
    sourceOfTruth: SourceOfTruth<Key, Response, Output>,
    private val cacheTimeout: Int = 0,
    private val timeUnit: TimeUnit = TimeUnit.MINUTES,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val timeProvider: TimeProvider = SystemTimeProvider()
) {
    private val cacheStartTime = mutableMapOf<Key, Long>()

    private val store: Store<Key, Output> = StoreBuilder
        .from(
            fetcher = Fetcher.ofFlow { requestFlow(it) },
            sourceOfTruth = sourceOfTruth,
        )
        .cachePolicy(
            MemoryPolicy.builder<Key, Output>()
                .setExpireAfterWrite(getCacheDuration())
                .build()
        )
        .build()

    fun cacheAndFreshWrapped(key: Key): Flow<StoreResponse<Output>> {
        return store.stream(StoreRequest.cached(key, refresh = !isMemoryCacheValid(key)))
    }

    fun cacheAndFresh(key: Key): Flow<EitherResult<Output>> {
        return cacheAndFreshWrapped(key).unwrap()
    }

    fun cacheOrFreshWrapped(key: Key): Flow<StoreResponse<Output>> {
        return store.stream(StoreRequest.cached(key, refresh = false))
    }

    /**
     * Note that this stream cannot be refreshed as it completely ignores Fetcher.
     * TODO it would be nice to add support for this.
     * Although it will receive new values from the sourceOfTruth
     */
    fun cacheOrFresh(key: Key): Flow<EitherResult<Output>> {
        return cacheOrFreshWrapped(key).unwrap()
    }

    suspend fun refresh(key: Key) {
        withContext(dispatcher) {
            launch { // launch returning immediately without waiting until freshResult ends
                fresh(key)
            }
        }
    }

    fun stream(request: StoreRequest<Key>): Flow<StoreResponse<Output>> =
        store.stream(request).flowOn(dispatcher)

    suspend fun get(key: Key): EitherResult<Output> = withContext(dispatcher) {
        runAsResult { store.get(key) }
    }

    suspend fun fresh(key: Key): EitherResult<Output> = withContext(dispatcher) {
        runAsResult { store.fresh(key) }
    }

    suspend fun clear(key: Key): Unit = withContext(dispatcher) { store.clear(key) }

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearAll(): Unit = withContext(dispatcher) { store.clearAll() }

    private fun requestFlow(key: Key) =
        flow { emit(request(key)) }
            .onEach { cacheStartTime[key] = timeProvider.currentTimeMillis() }
            .flowOn(dispatcher)

    private fun isMemoryCacheValid(key: Key): Boolean {
        val cacheStartTime = cacheStartTime[key] ?: 0
        val cacheTimeSoFar = timeProvider.currentTimeMillis() - cacheStartTime
        return cacheStartTime > 0 && cacheTimeSoFar < timeUnit.toMillis(cacheTimeout.toLong())
    }

    private fun getCacheDuration(): Duration {
        return cacheTimeout.toDuration(timeUnit.toDurationUnit())
    }

    private fun Flow<StoreResponse<Output>>.unwrap(): Flow<EitherResult<Output>> {
        return map { response ->
            when (response) {
                is StoreResponse.Data<Output> -> response.value.right()
                is StoreResponse.Error -> {
                    when (response) {
                        is StoreResponse.Error.Exception ->
                            StoreError.Exception(response.error).left()
                        is StoreResponse.Error.Message ->
                            StoreError.Message(response.message).left()
                    }
                }
                else -> null
            }
        }
            .filterNotNull()
    }
}


sealed class StoreError : DefaultError() {
    data class Exception(override val throwable: Throwable) : StoreError()
    data class Message(override val message: String) : StoreError()
}
