package com.rempawl.respolhpl.data.store

import arrow.core.left
import arrow.core.raise.catch
import arrow.core.right
import com.rempawl.respolhpl.utils.AppError
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.extensions.toEitherResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalCoroutinesApi
@ExperimentalTime
class ResponseStore<Key : Any, Response : Any, Output : Any>(
    private val request: suspend (Key) -> Response,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val timeProvider: TimeProvider = SystemTimeProvider(),
    sourceOfTruth: SourceOfTruth<Key, Response, Output>,
    cacheTimeout: Duration,
) {
    private val cacheStartTime = ConcurrentHashMap<Key, Long>()

    private val store: Store<Key, Output> = StoreBuilder
        .from(
            fetcher = Fetcher.ofFlow { requestFlow(it) },
            sourceOfTruth = sourceOfTruth,
        )
        .cachePolicy(
            MemoryPolicy.builder<Key, Output>()
                .setExpireAfterWrite(cacheTimeout)
                .build()
        )
        .build()

    private fun cacheAndFreshWrapped(key: Key): Flow<StoreReadResponse<Output>> {
        return store.stream(StoreReadRequest.cached(key = key, refresh = true))
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
        catch(
            block = { store.get(key).right() },
            catch = { it.toEitherResult() }
        )

    }

    suspend fun fresh(key: Key): EitherResult<Output> = withContext(dispatcher) {
        catch(
            block = { store.fresh(key).right() },
            catch = { it.toEitherResult() }
        )
    }

    suspend fun clear(key: Key): Unit = withContext(dispatcher) { store.clear(key) }

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clearAll(): Unit = withContext(dispatcher) { store.clear() }

    private fun requestFlow(key: Key) =
        flow { emit(request(key)) }
            .onEach { cacheStartTime[key] = timeProvider.currentTimeMillis() }
            .flowOn(dispatcher)

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

sealed class StoreError : AppError() {
    data class Exception(override val throwable: Throwable) : StoreError()
    data class Message(override val message: String) : StoreError()
}
