package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import kotlinx.coroutines.Deferred

interface RemoteDataSource {
    fun getAllProductsAsync(): Deferred<List<RemoteProductMinimal>>

    fun getProductByIDAsync(id: Int): Deferred<RemoteProduct>
}