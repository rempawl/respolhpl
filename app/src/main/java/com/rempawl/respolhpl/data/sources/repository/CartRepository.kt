package com.rempawl.respolhpl.data.sources.repository

import com.rempawl.respolhpl.data.model.database.CartProductEntity
import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.utils.extensions.EitherResult
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun delete(product: CartProductEntity)
    suspend fun clearCart()
    suspend fun addProduct(id: Int, quantity: Int)
    fun getCart() : Flow<EitherResult<List<CartProduct>>>
}

