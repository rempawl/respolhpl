package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.database.CartProductEntity
import com.example.respolhpl.data.model.domain.CartProduct
import com.example.respolhpl.utils.extensions.EitherResult
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun delete(product: CartProductEntity)
    suspend fun clearCart()
    suspend fun addProduct(id: Int, quantity: Int)
    fun getCart() : Flow<EitherResult<List<CartProduct>>>
}

