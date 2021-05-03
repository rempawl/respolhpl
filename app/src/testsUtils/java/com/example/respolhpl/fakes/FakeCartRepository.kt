package com.example.respolhpl.fakes

import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartRepository : CartRepository {
    private val prods = mutableListOf<CartProduct>()

    override suspend fun getProducts(): Flow<Result<*>> = flow {
        emit(Result.Success(prods))
    }


    override suspend fun addProduct(product: CartProduct) {
        prods.add(product)
    }

    override suspend fun delete(product: CartProduct) {
        TODO("Not yet implemented")
    }
}