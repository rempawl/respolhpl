package com.example.respolhpl.data.sources.remote

import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.RemoteProductMinimal

interface RemoteDataSource {

    suspend fun getProductById(id: Int): RemoteProduct

    suspend fun getProducts(
        perPage: Int, page: Int, status: String = DEFAULT_STATUS
    ): List<RemoteProductMinimal>

    companion object {
        const val DEFAULT_STATUS = "publish"
    }
}
