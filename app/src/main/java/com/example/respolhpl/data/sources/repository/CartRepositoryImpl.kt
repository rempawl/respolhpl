package com.example.respolhpl.data.sources.repository

import arrow.core.right
import arrow.fx.coroutines.parMap
import com.example.respolhpl.data.model.database.CartProductEntity
import com.example.respolhpl.data.model.domain.CartProduct
import com.example.respolhpl.data.model.remote.toDomain
import com.example.respolhpl.data.sources.local.CartDao
import com.example.respolhpl.data.sources.remote.WooCommerceApi
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.extensions.EitherResult
import com.example.respolhpl.utils.extensions.toResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val dao: CartDao,
    private val remoteDataSource: WooCommerceApi
) : CartRepository {

    override suspend fun addProduct(id: Int, quantity: Int) {
        withContext(dispatchersProvider.io) {
            dao.add(id, quantity)
        }
    }

    // todo make api that will support cart
    override fun getCart(): Flow<EitherResult<List<CartProduct>>> =
        dao.getCartProducts().mapLatest {
            it.parMap { cartProductEntity ->
                with(remoteDataSource.getProductById(cartProductEntity.id)) {
                    CartProduct(this.toDomain(), cartProductEntity.quantity)
                }
            }
                .right() as EitherResult<List<CartProduct>>
        }
            .catch { emit(it.toResult()) }
            .flowOn(dispatchersProvider.io)

    override suspend fun delete(product: CartProductEntity) {
        dao.delete(product)
    }

    override suspend fun clearCart() {
        dao.clearCart()
    }

}
