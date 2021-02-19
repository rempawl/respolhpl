package com.example.respolhpl.data.sources

import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product

interface Repository {

    suspend fun getProducts() : Result<*>
}