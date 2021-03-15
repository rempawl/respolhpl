package com.example.respolhpl.data.sources

import com.example.respolhpl.data.Result

interface ProductRepository {

    suspend fun getProducts(): Result<*>
    suspend fun getProductById(id: Int): Result<*>
}