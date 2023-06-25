package com.example.respolhpl.fakes

import com.example.respolhpl.data.sources.repository.CartRepository
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.model.domain.CartItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartRepository : CartRepository {
    private val prods = mutableListOf<CartItem.CartProduct>()

    override suspend fun getProducts(): Flow<Result<*>> = flow {
        emit(Result.Success(prods))
    }


    override suspend fun addProduct(product: CartItem.CartProduct) {
        prods.add(product)
    }

    override suspend fun delete(product: CartItem.CartProduct) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCart() {
        TODO("Not yet implemented")
    }
}