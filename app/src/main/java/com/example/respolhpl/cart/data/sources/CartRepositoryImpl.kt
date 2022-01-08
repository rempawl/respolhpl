package com.example.respolhpl.cart.data.sources

import com.example.respolhpl.cart.data.CartItem
import com.example.respolhpl.cart.data.CartProductEntity
import com.example.respolhpl.data.Result
import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartProductDao: CartProductDao,
    private val dispatchersProvider: DispatchersProvider
) : CartRepository {

    override suspend fun getProducts(): Flow<Result<*>> =
        cartProductDao.getCartProducts().catch { Result.Error(it) }
            .map { Result.Success(it.map { cartProductEntity -> CartItem.CartProduct.from(cartProductEntity) }) }

    override suspend fun addProduct(product: CartItem.CartProduct) {
        withContext(dispatchersProvider.io) {
            insertProductOrUpdateIfAlreadyIsInCart(product)
        }
    }

    override suspend fun delete(product: CartItem.CartProduct) {
        withContext(dispatchersProvider.io) {
            cartProductDao.delete(CartProductEntity.from(product))
        }
    }

    override suspend fun clearCart() {
        withContext(dispatchersProvider.io) {
            cartProductDao.deleteAll()
        }
    }

    private suspend fun insertProductOrUpdateIfAlreadyIsInCart(product: CartItem.CartProduct) {
        cartProductDao.getCartProductById(product.id).first()?.let { fromCart ->
            updateProduct(product.copy(quantity = fromCart.quantity + product.quantity))
        } ?: cartProductDao.insert(CartProductEntity.from(product))
    }

    private suspend fun updateProduct(product: CartItem.CartProduct) {
        cartProductDao.update(CartProductEntity.from(product))
    }


}