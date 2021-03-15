package com.example.respolhpl.data.sources

import android.util.Log
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.product.remote.RemoteProduct
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : ProductRepository {

    private fun transformRemoteProducts(res: List<RemoteProduct>) =
        res.map { remoteProduct -> Product.from(remoteProduct) }


    override suspend fun getProducts(): Result<*> {
        return try {
            val res = remoteDataSource.getAllProductsAsync().await()
            Result.Success(transformRemoteProducts(res))
        } catch (e: Exception) {
            Log.d("kruci",e.toString())
            Result.Error(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<*> {
        return try {
            val res = remoteDataSource.getProductByIdAsync(id).await()
            Result.Success(Product.from(res))
        } catch (e: Exception) {
            Log.d("kruci",e.toString())
            Result.Error(e)
        }
    }


}