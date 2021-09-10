package com.example.respolhpl.cart.data.sources

import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.data.Result
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getProducts(): Flow<Result<*>>
    suspend fun addProduct(product: CartProduct)
    suspend fun delete(product: CartProduct)
    suspend fun clearCart()
}

