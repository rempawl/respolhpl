package com.example.respolhpl.cart

import com.example.respolhpl.data.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface CartRepository {
    suspend fun getProducts(): Flow<Result<*>>

}

class CartRepositoryImpl @Inject constructor(private val cartProductDao: CartProductDao) :
    CartRepository {
    override suspend fun getProducts() =
        cartProductDao.getCartProducts().catch { Result.Error(it) }
            .map { Result.Success(it.map { cartProductEntity -> CartProduct.from(cartProductEntity) }) }


}