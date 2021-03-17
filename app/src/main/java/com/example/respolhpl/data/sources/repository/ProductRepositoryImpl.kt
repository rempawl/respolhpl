package com.example.respolhpl.data.sources.repository

import android.util.Log
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.local.ProductDao
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.product.ProductMinimal
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: ProductDao
) : ProductRepository {

    private fun transformRemoteProducts(res: List<RemoteProductMinimal>) =
        res.map { remoteProduct -> ProductMinimal.from(remoteProduct) }


    override suspend fun getProducts(): Result<*> {
        return try {
            val res = remoteDataSource.getAllProductsAsync().await()
            Result.Success(transformRemoteProducts(res))
        } catch (e: Exception) {
            Log.d("kruci", e.toString())
            Result.Error(e)
        }
    }

    override suspend fun getProductById(id: Int): Result<*> {
        return try {
            val res = remoteDataSource.getProductByIDAsync(id).await()
            Result.Success(Product.from(res))
        } catch (e: Exception) {
            Log.d("kruci", e.toString())
            Result.Error(e)
        }
    }


}