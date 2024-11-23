@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.rempawl.respolhpl.data.sources.repository

import com.rempawl.respolhpl.data.model.domain.ProductMinimal
import com.rempawl.respolhpl.data.model.domain.details.Product
import com.rempawl.respolhpl.data.model.domain.details.ProductVariant
import com.rempawl.respolhpl.data.model.remote.RemoteProduct
import com.rempawl.respolhpl.data.model.remote.RemoteProductMinimal
import com.rempawl.respolhpl.data.model.remote.RemoteProductVariant
import com.rempawl.respolhpl.data.model.remote.toDomain
import com.rempawl.respolhpl.data.sources.remote.WooCommerceApi
import com.rempawl.respolhpl.data.store.ResponseStore
import com.rempawl.respolhpl.data.store.SOTFactory
import com.rempawl.respolhpl.list.paging.PagingParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours
import kotlin.time.ExperimentalTime

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: WooCommerceApi,
    private val responseStoreFactory: ResponseStoreFactory,
    sotFactory: SOTFactory
) : ProductRepository {

    override val productDataStore: ResponseStore<Int, RemoteProduct, Product> by lazy {
        ResponseStore(
            request = { remoteDataSource.getProduct(it) },
            sourceOfTruth = sotFactory.create("product", mapper = { it.toDomain() }),
            cacheTimeout = 2.hours
        )
    }

    override val productVariantsStore: ResponseStore<Int, List<RemoteProductVariant>, List<ProductVariant>> by lazy {
        ResponseStore(
            request = { remoteDataSource.getProductVariations(it) },
            sourceOfTruth = sotFactory.create("variations", mapper = { it.toDomain() }),
            cacheTimeout = 2.hours,
        )
    }

    override val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>> by lazy {
        responseStoreFactory.productsDataStore
    }
}
