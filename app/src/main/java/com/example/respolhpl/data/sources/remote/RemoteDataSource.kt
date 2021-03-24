package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import kotlinx.coroutines.Deferred

interface RemoteDataSource {

    fun getProductByIDAsync(id: Int): Deferred<RemoteProduct>

    fun getProductsAsync(perPage: Int, page: Int): Deferred<List<RemoteProductMinimal>>
}