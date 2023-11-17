@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.RemoteProductMinimal
import com.example.respolhpl.data.model.remote.toDomain
import com.example.respolhpl.data.sources.remote.WooCommerceApi
import com.example.respolhpl.data.store.ResponseStore
import com.example.respolhpl.data.store.SOTFactory
import com.example.respolhpl.paging.PagingParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: WooCommerceApi,
    sotFactory: SOTFactory
) : ProductRepository {

    override val productDataStore: ResponseStore<Int, RemoteProduct, Product> =
        ResponseStore(
            request = { remoteDataSource.getProductById(it) },
            sourceOfTruth = sotFactory.create("product", mapper = { it.toDomain() }),
            cacheTimeout = 2,
            timeUnit = TimeUnit.HOURS
        )

    override val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>> =
        ResponseStore(
            request = { remoteDataSource.getProducts(it.perPage, it.page) },
            sourceOfTruth = sotFactory.create("products", mapper = { it.toDomain() }),
            cacheTimeout = 1,
            timeUnit = TimeUnit.HOURS
        )

}
