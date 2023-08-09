package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.domain.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getProducts(): Flow<Result<*>>
    suspend fun addProduct(product: CartItem.CartProduct)
    suspend fun delete(product: CartItem.CartProduct)
    suspend fun clearCart()
}

