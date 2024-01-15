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
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.ExperimentalTime


@Singleton
class ResponseStoreFactory @Inject constructor(
    private val remoteDataSource: WooCommerceApi,
    private val sotFactory: SOTFactory
) {
    //    private val allStores = mutableListOf<ResponseStore<*, *, *>>()
    //    todo save cache

//
//    fun <Key : Any, Response : Any, Output : Any> createStore(
//        request: suspend (Key) -> Response,
//        cacheTimeout: Duration = Duration.ZERO,
//        dispatcher: CoroutineDispatcher = Dispatchers.IO,
//        clock: Clock = Clock.System,
//        sourceOfTruth: DiskSOTFactory.() -> SourceOfTruth<Key, Response, Output>,
//    ): ResponseStore<Key, Response, Output> {
//        return ResponseStore(
//            request,
//            sourceOfTruth(sotFactory),
//            cacheTimeout,
//            dispatcher,
//            clock
//        ).also {
//            allStores.add(it)
//        }
//    }
    val productDataStore = ResponseStore<Int, RemoteProduct, Product>(
        request = { remoteDataSource.getProductById(it) },
        sourceOfTruth = sotFactory.create("product", mapper = { it.toDomain() }),
        cacheTimeout = 2,
        timeUnit = TimeUnit.HOURS
    )

    val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>> by lazy {
        ResponseStore(
            request = { remoteDataSource.getProducts(it.perPage, it.page) },
            sourceOfTruth = sotFactory.create("products", mapper = { it.toDomain() }),
        )
    }

}

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: WooCommerceApi,
    private val responseStoreFactory: ResponseStoreFactory,
    sotFactory: SOTFactory
) : ProductRepository {

    override val productDataStore: ResponseStore<Int, RemoteProduct, Product> =
        ResponseStore(
            request = { remoteDataSource.getProductById(it) },
            sourceOfTruth = sotFactory.create("product", mapper = { it.toDomain() }),
            cacheTimeout = 2,
            timeUnit = TimeUnit.HOURS
        )
    override val productVariantsStore: ResponseStore<Int, List<RemoteProductVariant>, List<ProductVariant>> =
        ResponseStore(
            request = { remoteDataSource.getProductVariations(it) },
            sourceOfTruth = sotFactory.create("variations", mapper = { it.toDomain() }),
            cacheTimeout = 2,
            timeUnit = TimeUnit.HOURS
        )

    override val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>> by lazy {
        responseStoreFactory.productsDataStore
    }
//        ResponseStore(
//            request = { remoteDataSource.getProducts(it.perPage, it.page) },
//            sourceOfTruth = sotFactory.create("products", mapper = { it.toDomain() }),
//            cacheTimeout = 2137,
//            timeUnit = TimeUnit.DAYS
//        )

}
