@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.toDomain
import com.example.respolhpl.data.paging.PagingData
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.store.ResponseStore
import com.example.respolhpl.data.store.SOTFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
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

    override suspend fun getProducts(): Flow<PagingData<ProductMinimal>> {
        return flowOf(PagingData())
//        return productsPagerFactory.create().flow
    }

    companion object {
        const val TIMEOUT: Long = 10_000
    }

}

