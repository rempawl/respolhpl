package com.example.respolhpl.data.sources.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.ProductPagingSourceImpl.Companion.NETWORK_PAGE_SIZE
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject


class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
//    private val localDataSource: ProductDao, todo favs
    private val dispatchersProvider: DispatchersProvider
//    private val productsPagingSourceFactory: ProductPagingSourceFactory todo
) : ProductRepository {

    private suspend fun getDataAsFlow(getter: suspend () -> Result<*>) =
        flow {
            emit(getter())
        }.flowOn(dispatchersProvider.io)


    private suspend fun <T> getDataWithTimeout(getter: suspend () -> T): T = withTimeout(10_000) {
        getter()
    }

    override suspend fun getProducts(): Flow<PagingData<ProductMinimal>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                ProductPagingSourceImpl(remoteDataSource) {
                    ProductMinimal.from(
                        it
                    )
                }
            }
            //todo inject

        ).flow
    }


    override suspend fun getProductById(id: Int): Flow<Result<*>> = getDataAsFlow {
        try {
            val res = getDataWithTimeout { remoteDataSource.getProductByIDAsync(id) }
            Result.Success(Product.from(res))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getProductImages(id: Int): Flow<Result<*>> = getDataAsFlow {
        try {
            val res = getDataWithTimeout { remoteDataSource.getProductByIDAsync(id).images }
            val imgs = res.map { Image.from(it) }
            Result.Success(imgs)
        } catch (e: Exception) {
            Result.Error(e)
        }


    }

}
