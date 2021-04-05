package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import kotlinx.coroutines.Deferred

interface RemoteDataSource {

    suspend fun getProductByIDAsync(id: Int): RemoteProduct

    suspend fun getProductsAsync(perPage: Int, page: Int): List<RemoteProductMinimal>
}