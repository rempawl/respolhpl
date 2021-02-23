package com.example.respolhpl.data.sources

import com.example.respolhpl.data.Result

interface Repository {

    suspend fun getProducts(): Result<*>
    suspend fun getProductById(id: Long): Result<*>
}