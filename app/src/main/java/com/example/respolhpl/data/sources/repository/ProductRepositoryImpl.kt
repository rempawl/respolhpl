package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
//    private val localDataSource: ProductDao, todo favs
    private val dispatchersProvider: DispatchersProvider,
    private val productsPagerFactory: ProductsPagerFactory
) : ProductRepository {

    private suspend fun getDataAsFlow(getter: suspend () -> Result<*>) =
        flow {
            emit(getter())
        }.flowOn(dispatchersProvider.io)


    private suspend fun <T> getDataWithTimeout(getter: suspend () -> T): T = withTimeout(10_000) {
        getter()
    }

    override suspend fun getProducts(): Flow<PagingData<ProductMinimal>> {
        return productsPagerFactory.create().flow
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
