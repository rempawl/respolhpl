package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.Result
import kotlinx.coroutines.flow.Flow

interface ProductRepository {

    suspend fun getProducts(): Flow<Result<*>>
    suspend fun getProductById(id: Int): Flow<Result<*>>
}