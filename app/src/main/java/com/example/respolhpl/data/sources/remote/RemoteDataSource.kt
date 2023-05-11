package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.WooCommerceApi.Companion.DEFAULT_STATUS

interface RemoteDataSource {

    suspend fun getProductById(id: Int): RemoteProduct

    suspend fun getProducts(
        perPage: Int, page: Int, status: String = DEFAULT_STATUS
    ): List<RemoteProductMinimal>
}
