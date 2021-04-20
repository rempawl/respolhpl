package com.example.respolhpl.cart.data.sources

import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(private val cartProductDao: CartProductDao) :
    CartRepository {
    override suspend fun getProducts() =
        cartProductDao.getCartProducts().catch { Result.Error(it) }
            .map { Result.Success(it.map { cartProductEntity -> CartProduct.from(cartProductEntity) }) }

    override suspend fun addProduct(product: CartProduct) {

    }


}