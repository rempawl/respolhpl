package com.rempawl.respolhpl.data.store

import arrow.core.left
import arrow.core.raise.either
import arrow.core.right
import com.rempawl.respolhpl.utils.DefaultError
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.log
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.MemoryPolicy
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import java.util.concurrent.ConcurrentHashMap
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
    private val cacheStartTime = ConcurrentHashMap<Key, Long>()

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
    private fun cacheAndFreshWrapped(key: Key): Flow<StoreReadResponse<Output>> {
        return store.stream(StoreReadRequest.cached(key = key, refresh = !isMemoryCacheValid(key)))
    }

    fun cacheAndFresh(key: Key): Flow<EitherResult<Output>> {
        return cacheAndFreshWrapped(key).unwrap()
    }

    suspend fun refresh(key: Key) {
        withContext(dispatcher) {
            launch {
                fresh(key)
            }
        }
    }

    suspend fun get(key: Key): EitherResult<Output> = withContext(dispatcher) {
        either { store.get(key) }
    }

    suspend fun fresh(key: Key): EitherResult<Output> = withContext(dispatcher) {
        either { store.fresh(key) }
    }

    suspend fun clear(key: Key): Unit = withContext(dispatcher) { store.clear(key) }

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearAll(): Unit = withContext(dispatcher) { store.clear() }

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

    private fun Flow<StoreReadResponse<Output>>.unwrap(): Flow<EitherResult<Output>> {
        return map { response ->
            when (response) {
                is StoreReadResponse.Data<Output> -> response.value.right()
                is StoreReadResponse.Error -> {
                    when (response) {
                        is StoreReadResponse.Error.Exception ->
                            StoreError.Exception(response.error).left()

                        is StoreReadResponse.Error.Message ->
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
