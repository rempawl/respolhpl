package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteDataSource {

    suspend fun getProductByIDAsync(id: Int): RemoteProduct

    suspend fun getProductsAsync(perPage: Int, page: Int): List<RemoteProductMinimal>
}