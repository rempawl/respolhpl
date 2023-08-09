package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.domain.CartItem
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider
) : CartRepository {

    override suspend fun getProducts(): Flow<Result<*>> = emptyFlow()

    override suspend fun addProduct(product: CartItem.CartProduct) {
        withContext(dispatchersProvider.io) {
            // todo room?
        }
    }

    override suspend fun delete(product: CartItem.CartProduct) {
    }

    override suspend fun clearCart() {
    }


    private suspend fun updateProduct(product: CartItem.CartProduct) {
    }


}
