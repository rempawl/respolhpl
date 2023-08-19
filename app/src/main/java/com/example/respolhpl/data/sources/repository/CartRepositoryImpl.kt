package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.domain.CartItem
import com.example.respolhpl.data.sources.local.CartDao
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val dao: CartDao
) : CartRepository {

    override suspend fun getProducts() = dao.getCartProducts()

    override suspend fun addProduct(id: Int, quantity: Int) {
        withContext(dispatchersProvider.io) {
            dao.add(id, quantity)
        }
    }

    override suspend fun delete(product: CartItem.CartProduct) {
    }

    override suspend fun clearCart() {
    }


}
