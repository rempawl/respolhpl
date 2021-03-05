package com.example.respolhpl.data.sources

import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.product.remote.RemoteProduct
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : Repository {

    private fun transformRemoteProducts(res: List<RemoteProduct>) =
        res.map { remoteProduct -> Product.from(remoteProduct) }


    override suspend fun getProducts(): Result<*> {
        return try {
            val res = remoteDataSource.getAllProductsAsync().await()
            Result.Success(transformRemoteProducts(res))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getProductById(id: Long): Result<*> {
        return try {
            val res = remoteDataSource.getProductByIdAsync(id).await()
            Result.Success(Product.from(res))
        } catch (e: Exception) {
            Result.Error(e)
        }
    }


}