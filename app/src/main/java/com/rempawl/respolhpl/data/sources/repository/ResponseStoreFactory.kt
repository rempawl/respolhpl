package com.rempawl.respolhpl.data.sources.repository

import com.rempawl.respolhpl.data.model.domain.ProductMinimal
import com.rempawl.respolhpl.data.model.remote.RemoteProductMinimal
import com.rempawl.respolhpl.data.model.remote.toDomain
import com.rempawl.respolhpl.data.sources.remote.WooCommerceApi
import com.rempawl.respolhpl.data.store.ResponseStore
import com.rempawl.respolhpl.data.store.SOTFactory
import com.rempawl.respolhpl.list.paging.PagingParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class ResponseStoreFactory @Inject constructor(
    private val remoteDataSource: WooCommerceApi,
    private val sotFactory: SOTFactory
) {
    @OptIn(ExperimentalTime::class)
    val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>> by lazy {
        ResponseStore(
            request = { remoteDataSource.getProducts(it.perPage, it.page) },
            sourceOfTruth = sotFactory.create("products", mapper = { it.toDomain() }),
            cacheTimeout = 2.hours
        )
    }

}