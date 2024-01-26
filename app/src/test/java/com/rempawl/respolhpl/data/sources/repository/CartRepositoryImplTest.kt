package com.rempawl.respolhpl.data.sources.repository

import app.cash.turbine.test
import arrow.core.right
import com.rempawl.respolhpl.TestDispatchersProvider
import com.rempawl.respolhpl.data.model.database.CartProductEntity
import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.data.model.domain.details.Product
import com.rempawl.respolhpl.data.model.domain.details.ProductType
import com.rempawl.respolhpl.data.model.remote.RemoteProduct
import com.rempawl.respolhpl.data.model.remote.RemoteProductVariant
import com.rempawl.respolhpl.data.sources.local.CartDao
import com.rempawl.respolhpl.data.sources.remote.WooCommerceApi
import com.rempawl.respolhpl.utils.assertItemEquals
import com.rempawl.respolhpl.utils.coVerifyNever
import com.rempawl.respolhpl.utils.coVerifyOnce
import com.rempawl.respolhpl.utils.extensions.asList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CartRepositoryImplTest {

    private val dao = mockk<CartDao>(relaxUnitFun = true)

    private val remoteDataSource = mockk<WooCommerceApi>()

    private val testDispatchersProvider = TestDispatchersProvider()

    private fun createSUT(
        cartEntities: List<CartProductEntity> = emptyList(),
        cartError: Throwable? = null,
        variantError: Throwable? = null,
        productError: Throwable? = null
    ): CartRepositoryImpl {
        dao.mock(cartEntities, cartError)
        remoteDataSource.mockProduct(productError)
        remoteDataSource.mockVariant(variantError)

        return CartRepositoryImpl(
            dispatchersProvider = testDispatchersProvider,
            dao = dao,
            remoteDataSource = remoteDataSource
        )
    }

    @Test
    fun `given empty cart,when getCart called, then correct result`() =
        runTest(context = testDispatchersProvider.test) {
            createSUT()
                .getCart()
                .test {
                    assertItemEquals(emptyList<CartProduct>().right())
                    awaitComplete()
                }
        }

    @Test
    fun `given simple products in cart, when getCart called, then correct result`() =
        runTest(context = testDispatchersProvider.test) {
            createSUT(cartEntities = SIMPLE_PRODUCT_ENTITY.asList())
                .getCart()
                .test {
                    assertItemEquals(
                        listOf(
                            CartProduct(
                                product = Product(
                                    id = 1,
                                    name = "name",
                                    price = 10.0,
                                    images = emptyList(),
                                    quantity = 10,
                                    description = "description",
                                    productType = ProductType.SIMPLE,
                                    thumbnailSrc = null
                                ),
                                quantity = 1,
                                variantId = null
                            )
                        ).right()
                    )
                    awaitComplete()
                }

        }

    @Test
    fun `given variable products in cart, when getCart called, then correct result`() =
        runTest(testDispatchersProvider.test) {
            createSUT(cartEntities = VARIABLE_PRODUCT_ENTITY.asList())
                .getCart()
                .test {
                    assertItemEquals(
                        listOf(
                            CartProduct(
                                product = Product(
                                    id = 2,
                                    name = "name",
                                    price = 10.0,
                                    images = emptyList(),
                                    quantity = 10,
                                    description = "description",
                                    productType = ProductType.VARIABLE,
                                    thumbnailSrc = null
                                ),
                                quantity = 1,
                                variantId = 111
                            )
                        ).right()
                    )
                    awaitComplete()
                }

        }

    @Test
    fun `given simple and variable products in cart, when getCart called, then correct result`() =
        runTest(testDispatchersProvider.test) {
            createSUT(cartEntities = listOf(SIMPLE_PRODUCT_ENTITY, VARIABLE_PRODUCT_ENTITY))
                .getCart()
                .test {
                    assertItemEquals(
                        listOf(
                            CartProduct(
                                product = Product(
                                    id = 1,
                                    name = "name",
                                    price = 10.0,
                                    images = emptyList(),
                                    quantity = 10,
                                    description = "description",
                                    productType = ProductType.SIMPLE,
                                    thumbnailSrc = null
                                ),
                                quantity = 1,
                                variantId = null
                            ),
                            CartProduct(
                                product = Product(
                                    id = 2,
                                    name = "name",
                                    price = 10.0,
                                    images = emptyList(),
                                    quantity = 10,
                                    description = "description",
                                    productType = ProductType.VARIABLE,
                                    thumbnailSrc = null
                                ),
                                quantity = 1,
                                variantId = 111
                            )
                        ).right()
                    )
                    awaitComplete()
                }

        }

    @Test
    fun `given error in dao, when getCart called, then correct result`() =
        runTest(testDispatchersProvider.test) {
            createSUT(cartError = THROWABLE)
                .getCart()
                .test {
                    awaitItem().run {
                        assertTrue { this.isLeft() }
                    }
                    awaitComplete()
                }

        }

    @Test
    fun `given error in remoteDataSource, when getCart called, then correct result`() =
        runTest(testDispatchersProvider.test) {
            createSUT(
                cartEntities = listOf(VARIABLE_PRODUCT_ENTITY),
                productError = THROWABLE
            )
                .getCart()
                .test {
                    awaitItem().run {
                        assertTrue { this.isLeft() }
                    }
                    awaitComplete()
                }

        }

    @Test
    fun `given error while fetching variant, when getCart called, then correct result`() =
        runTest(testDispatchersProvider.test) {
            createSUT(
                cartEntities = listOf(VARIABLE_PRODUCT_ENTITY),
                variantError = THROWABLE
            )
                .getCart()
                .test {
                    awaitItem().run {
                        assertTrue { this.isLeft() }
                    }
                    awaitComplete()
                }

        }

    @Test
    fun `given error in both dao and remoteDataSource, when getCart called, then correct result`() =
        runTest(testDispatchersProvider.test) {
            createSUT(
                cartEntities = listOf(VARIABLE_PRODUCT_ENTITY),
                cartError = THROWABLE,
                productError = THROWABLE
            )
                .getCart()
                .test {
                    awaitItem().run {
                        assertTrue { this.isLeft() }
                    }
                    awaitComplete()
                }
        }


    @Test
    fun `when variable product added , then correct dao methods called`() =
        runTest(testDispatchersProvider.test) {
            val sut = createSUT()
            coVerifyNever { dao.add(id = any(), quantity = any(), type = any(), variantId = any()) }

            sut.addProduct(
                id = 2,
                quantity = 1,
                type = ProductType.VARIABLE,
                variantId = 111
            )

            coVerifyOnce {
                dao.add(
                    id = 2,
                    quantity = 1,
                    type = ProductType.VARIABLE,
                    variantId = 111
                )
            }
        }

    @Test
    fun `when simple product added, then correct dao methods called`() =
        runTest(testDispatchersProvider.test) {
            val sut = createSUT()
            coVerifyNever { dao.add(id = any(), quantity = any(), type = any(), variantId = any()) }

            sut.addProduct(
                id = 1,
                quantity = 1,
                type = ProductType.SIMPLE,
                variantId = null
            )

            coVerifyOnce {
                dao.add(id = 1, quantity = 1, type = ProductType.SIMPLE, variantId = 0)
            }
        }

    @Test
    fun `when delete called, then correct dao methods called`() =
        runTest(testDispatchersProvider.test) {
            val sut = createSUT()
            coVerifyNever { dao.delete(any()) }

            sut.delete(
                id = 1, quantity = 1, type = ProductType.SIMPLE, variantId = 0
            )

            coVerifyOnce {
                dao.delete(
                    CartProductEntity(
                        id = 1,
                        quantity = 1,
                        type = ProductType.SIMPLE,
                        variantId = 0
                    )
                )
            }

        }

    @Test
    fun `when clearCart called, then correct dao methods called`() =
        runTest(testDispatchersProvider.test) {
            val sut = createSUT()
            coVerifyNever { dao.clearCart() }

            sut.clearCart()

            coVerifyOnce { dao.clearCart() }
        }


    private fun WooCommerceApi.mockVariant(error: Throwable? = null) {
        if (error != null) {
            coEvery { getProductVariant(any(), any()) } throws error
        } else {
            coEvery { getProductVariant(any(), any()) } returns RemoteProductVariant(
                price = 10.0,
                id = 111,
                attributes = emptyList(),
                stock_quantity = 10
            )
        }
    }

    private fun WooCommerceApi.mockProduct(error: Throwable? = null) {
        if (error != null) {
            coEvery { getProduct(any()) } throws error
        } else {
            coEvery { getProduct(1) } returns RemoteProduct(
                id = 1,
                name = "name",
                price = 10.0,
                images = emptyList(),
                stock_quantity = 10,
                description = "description",
                type = "simple"
            )

            coEvery {
                getProduct(2)
            } returns RemoteProduct(
                id = 2,
                name = "name",
                price = 10.0,
                images = emptyList(),
                stock_quantity = 10,
                description = "description",
                type = "variable"
            )
        }
    }


    private fun CartDao.mock(entities: List<CartProductEntity>, error: Throwable? = null) {
        if (error != null) {
            every { getCartProducts() } returns flow { throw error }
        } else {
            every { getCartProducts() } returns flowOf(entities)
        }
    }

    private companion object {
        val THROWABLE = Throwable("error")
        val SIMPLE_PRODUCT_ENTITY = CartProductEntity(
            id = 1,
            quantity = 1,
            type = ProductType.SIMPLE,
            variantId = 0
        )
        val VARIABLE_PRODUCT_ENTITY = CartProductEntity(
            id = 2,
            quantity = 1,
            type = ProductType.VARIABLE,
            variantId = 111
        )
    }
}

