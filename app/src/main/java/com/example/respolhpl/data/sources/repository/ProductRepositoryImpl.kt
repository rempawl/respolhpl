package com.example.respolhpl.data.sources.repository

import android.util.Log
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.product.ProductMinimal
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.local.ProductDao
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: ProductDao,
    private val dispatchersProvider: DispatchersProvider
) : ProductRepository {
    //TODO REFACTOR

    private fun transformRemoteProducts(res: List<RemoteProductMinimal>) =
        res.map { remoteProduct -> ProductMinimal.from(remoteProduct) }


    private fun flowBuilder(block: suspend () -> Result<*>) = flow {
        emit(block())
    }

    override suspend fun getProducts(): Flow<Result<*>> = withContext(dispatchersProvider.io) {
        flowBuilder {
            try {
                val res = remoteDataSource.getAllProductsAsync().await()
                Result.Success(transformRemoteProducts(res))
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    override suspend fun getProductById(id: Int): Flow<Result<*>> =
        withContext(dispatchersProvider.io) {
            flowBuilder {
                try {
                    val res = remoteDataSource.getProductByIDAsync(id).await()
                    Result.Success(Product.from(res))
                } catch (e: Exception) {
                    Result.Error(e)
                }
            }
        }
}