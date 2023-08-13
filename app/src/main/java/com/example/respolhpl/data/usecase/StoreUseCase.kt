@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.respolhpl.data.usecase

import com.dropbox.android.external.store4.StoreRequest
import com.dropbox.android.external.store4.StoreResponse
import com.example.respolhpl.data.store.ResponseStore
import com.example.respolhpl.utils.extensions.EitherResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
abstract class StoreUseCase<Key : Any, Output : Any>(
    private val store: ResponseStore<Key, *, Output>,
) {

    fun cacheAndFresh(key: Key): Flow<EitherResult<Output>> =
        store.cacheAndFresh(key)

    suspend fun refresh(key: Key) = store.refresh(key)

    suspend fun get(key: Key): EitherResult<Output> = store.get(key)

    suspend fun fresh(key: Key): EitherResult<Output> = store.fresh(key)

    suspend fun clear(key: Key): Unit = store.clear(key)

    suspend fun clearAll(): Unit = store.clearAll()
}
