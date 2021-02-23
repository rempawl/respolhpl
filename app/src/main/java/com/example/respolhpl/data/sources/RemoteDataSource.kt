package com.example.respolhpl.data.sources

import com.example.respolhpl.data.product.remote.RemoteProduct
import kotlinx.coroutines.Deferred

interface RemoteDataSource {
    fun getAllProductsAsync() : Deferred<List<RemoteProduct>>
}