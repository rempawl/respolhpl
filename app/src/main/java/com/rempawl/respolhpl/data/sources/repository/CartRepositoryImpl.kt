package com.rempawl.respolhpl.data.sources.repository

import arrow.core.right
import arrow.fx.coroutines.parMap
import com.rempawl.respolhpl.data.model.database.CartProductEntity
import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.data.model.domain.details.Product
import com.rempawl.respolhpl.data.model.domain.details.ProductType
import com.rempawl.respolhpl.data.model.remote.toDomain
import com.rempawl.respolhpl.data.sources.local.CartDao
import com.rempawl.respolhpl.data.sources.remote.WooCommerceApi
import com.rempawl.respolhpl.utils.DispatchersProvider
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.extensions.toEitherResult
import com.rempawl.respolhpl.utils.extensions.zipAsync
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImpl @Inject constructor(
    private val dispatchersProvider: DispatchersProvider,
    private val dao: CartDao,
    private val remoteDataSource: WooCommerceApi
) : CartRepository {

    override suspend fun addProduct(id: Int, quantity: Int, type: ProductType, variantId: Int?) =
        dao.add(id, quantity, type, variantId ?: 0)

    override suspend fun delete(id: Int, quantity: Int, type: ProductType, variantId: Int?) =
        dao.delete(
            CartProductEntity(
                id = id,
                variantId = variantId ?: 0,
                quantity = quantity,
                type = type,
            )
        )

    override suspend fun clearCart() = dao.clearCart()

    // todo make api that will support cart (:
    @Suppress("USELESS_CAST")
    override fun getCart(): Flow<EitherResult<List<CartProduct>>> =
        dao.getCartProducts()
            .mapLatest {
                it.parMap { cartProductEntity ->
                    when (cartProductEntity.type) {
                        ProductType.SIMPLE -> getSimpleProduct(cartProductEntity)
                        ProductType.VARIABLE -> getVariableProduct(cartProductEntity)
                    }
                }
                    .right() as EitherResult<List<CartProduct>>
            }
            .catch { emit(it.toEitherResult()) }
            .flowOn(dispatchersProvider.io)

    private suspend fun getVariableProduct(cartProductEntity: CartProductEntity): CartProduct =
        zipAsync(
            firstCaller = { remoteDataSource.getProduct(cartProductEntity.id).toDomain() },
            secondCaller = {
                remoteDataSource.getProductVariant(
                    id = cartProductEntity.id,
                    variantId = cartProductEntity.variantId
                ).toDomain()
            },
            mapper = { product, variant ->
                CartProduct(
                    product = Product(
                        id = product.id,
                        price = variant.price,
                        quantity = variant.quantity,
                        images = product.images,
                        thumbnailSrc = product.images.firstOrNull()?.src,
                        description = product.description,
                        name = product.name,
                        productType = product.productType
                    ),
                    quantity = cartProductEntity.quantity,
                    variantId = variant.id
                )
            }
        )


    private suspend fun getSimpleProduct(cartProductEntity: CartProductEntity) =
        with(remoteDataSource.getProduct(cartProductEntity.id)) {
            CartProduct(this.toDomain(), cartProductEntity.quantity, null)
        }

}
