@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.RemoteProductMinimal
import com.example.respolhpl.data.model.remote.toDomain
import com.example.respolhpl.paging.PagingParam
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.store.ResponseStore
import com.example.respolhpl.data.store.SOTFactory
import com.example.respolhpl.data.usecase.StoreUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
//    private val productsPagerFactory: ProductsPagerFactory,
    sotFactory: SOTFactory
) : ProductRepository {

    override val productDataStore: ResponseStore<Int, RemoteProduct, Product> =
        ResponseStore(
            { remoteDataSource.getProductById(it) },
            sotFactory.create("product", mapper = { it.toDomain() })
        )
    override val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>> =
        ResponseStore(
            { remoteDataSource.getProducts(it.perPage, it.page) },
            sotFactory.create("products", mapper = { it.toDomain() })
        )


    companion object {
        const val TIMEOUT: Long = 10_000
    }

}


class GetProductsUseCase @Inject constructor(private val repo: ProductRepository) :
    StoreUseCase<PagingParam, List<ProductMinimal>>(repo.productsDataStore)
