package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.database.CartProductEntity
import com.example.respolhpl.data.model.domain.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun getProducts(): Flow<List<CartProductEntity>>
    suspend fun delete(product: CartItem.CartProduct)
    suspend fun clearCart()
    suspend fun addProduct(id: Int, quantity: Int)
}

