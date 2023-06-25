@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.ImageRemote
import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.toDomain
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
import com.example.respolhpl.data.store.ResponseStore
import com.example.respolhpl.data.store.SOTFactory
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.mappers.facade.MappersFacade
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val dispatchersProvider: DispatchersProvider,
    private val productsPagerFactory: ProductsPagerFactory,
    private val mappers: MappersFacade,
    sotFactory: SOTFactory
) : ProductRepository {

    override val productDataStore: ResponseStore<Int, RemoteProduct, Product> =
        ResponseStore(
            { remoteDataSource.getProductById(it) },
            sotFactory.create("product", mapper = { it.toDomain() })
        )

    override val productImagesDataStore: ResponseStore<Int, List<ImageRemote>, List<Image>> =
        ResponseStore(
            { remoteDataSource.getProductById(it).images },
            sotFactory.create("product_images", mapper = { it.toDomain() })
        )

    override suspend fun getProducts(): Flow<PagingData<ProductMinimal>> {
        return productsPagerFactory.create().flow
    }

    companion object {
        const val TIMEOUT: Long = 10_000
    }

}

