package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(): Flow<PagingData<ProductMinimal>>
    suspend fun getProductById(id: Int): Flow<Result<*>>
}