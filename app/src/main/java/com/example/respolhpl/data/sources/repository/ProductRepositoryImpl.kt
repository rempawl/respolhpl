package com.example.respolhpl.data.sources.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.ProductPagingSource.Companion.NETWORK_PAGE_SIZE
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
//    private val localDataSource: ProductDao, todo favs
    private val dispatchersProvider: DispatchersProvider
) : ProductRepository {

    private suspend fun getData(getter: suspend () -> Result<*>) =
        withContext(dispatchersProvider.io) {
            flow {
                emit(getter())
            }
        }

    override suspend fun getProducts(): Flow<PagingData<ProductMinimal>> {
        return Pager(config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
            pagingSourceFactory = {
                ProductPagingSource(remoteDataSource) { res -> ProductMinimal.from(res) }
            }
        ).flow
    }


    override suspend fun getProductById(id: Int): Flow<Result<*>> = getData {
        try {
            val res = remoteDataSource.getProductByIDAsync(id)
            Result.Success(Product.from(res))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getProductImages(id: Int): Flow<Result<*>> = getData {
        try {
            val res = remoteDataSource.getProductByIDAsync(id).images
            val imgs = res.map { Image.from(it) }
            Result.Success(imgs)
        } catch (e: java.lang.Exception) {
            Result.Error(e)
        }


    }

}
