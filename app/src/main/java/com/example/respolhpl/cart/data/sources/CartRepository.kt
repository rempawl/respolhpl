package com.example.respolhpl.cart.data.sources

import com.example.respolhpl.cart.data.CartItem
import com.example.respolhpl.data.Result
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getProducts(): Flow<Result<*>>
    suspend fun addProduct(product: CartItem.CartProduct)
    suspend fun delete(product: CartItem.CartProduct)
    suspend fun clearCart()
}

