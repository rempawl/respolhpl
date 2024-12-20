package com.rempawl.respolhpl.cart

import app.cash.turbine.test
import com.rempawl.respolhpl.cart.CartViewModel.CartEffects.NavigateToCheckout
import com.rempawl.respolhpl.cart.CartViewModel.CartEffects.ShowClearCartConfirmationDialog
import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.data.model.domain.details.Product
import com.rempawl.respolhpl.data.model.domain.details.ProductType
import com.rempawl.respolhpl.data.usecase.ClearCartUseCase
import com.rempawl.respolhpl.data.usecase.GetCartProductsUseCase
import com.rempawl.respolhpl.utils.AppError
import com.rempawl.respolhpl.utils.BaseCoroutineTest
import com.rempawl.respolhpl.utils.PriceFormatter
import com.rempawl.respolhpl.utils.ProgressSemaphoreImpl
import com.rempawl.respolhpl.utils.coVerifyNever
import com.rempawl.respolhpl.utils.coVerifyOnce
import com.rempawl.respolhpl.utils.mockFlowResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class CartViewModelTest : BaseCoroutineTest() {

    private val getCartProductsUseCase = mockk<GetCartProductsUseCase>()
    private val clearCartUseCase = mockk<ClearCartUseCase>(relaxed = true)

    private fun createSUT(): CartViewModel {
        return CartViewModel(
            getCartProductsUseCase = getCartProductsUseCase,
            cartFormatter = CartFormatter(PriceFormatter()),
            clearCartUseCase = clearCartUseCase,
            ProgressSemaphoreImpl()
        )
    }

    @Test
    fun `when init, then show and hide progress`() = runTest {
        getCartProductsUseCase.mock(response = ITEMS, delay = DELAY)
        createSUT().state.test {
            assertTrue { awaitItem().isLoading }

            advanceTimeBy(DELAY + 1)

            assertFalse { awaitItem().isLoading }
        }
    }

    @Test
    fun `when initialized, then correct state set`() = runTest {
        getCartProductsUseCase.mock()
        createSUT().state.test {
            expectMostRecentItem().run {
                assertEquals(2, cartItems.size)
                cartItems[0].run {
                    assertIs<CartItem.Product>(this)
                    assertEquals(name, "name")
                    assertEquals(priceFormatted, "10.00 PLN")
                    assertEquals(thumbnailSrc, "src")
                    assertEquals(quantityFormatted, "2")
                    assertEquals(cost, "20.00 PLN")
                }
                cartItems[1].run {
                    assertIs<CartItem.Summary>(this)
                    assertEquals(cost, "20.00 PLN")
                }
                assertTrue { isClearCartBtnVisible }
                assertFalse { isEmptyPlaceholderVisible }
            }
        }
    }

    @Test
    fun `when no items returned then empty state visible`() = runTest {
        getCartProductsUseCase.mock(response = emptyList())
        createSUT().state.test {
            awaitItem().run {
                assertTrue { isEmptyPlaceholderVisible }
                assertFalse { isClearCartBtnVisible }
            }
        }
    }

    @Test
    fun `when init fails, then error is displayed`() = runTest {
        getCartProductsUseCase.mock(response = emptyList(), error = TEST_ERROR, delay = DELAY)

        createSUT().state.test {
            assertNull(awaitItem().error)

            advanceTimeBy(DELAY + 1)

            assertEquals(TEST_ERROR, awaitItem().error)
        }
    }

    @Test
    fun `when buy clicked, then navigate to checkout effect set`() = runTest {
        getCartProductsUseCase.mock()
        val sut = createSUT()
        sut.effects.test {
            expectNoEvents()

            sut.onBuyClick()

            assertIs<NavigateToCheckout>(expectMostRecentItem())
        }
    }

    @Test
    fun `given error, when retry click and success, then correct state set`() = runTest {
        getCartProductsUseCase.mock(error = TEST_ERROR)
        val sut = createSUT()
        sut.state.test {
            assertNotNull(expectMostRecentItem().error)
            getCartProductsUseCase.mock()

            sut.retry()

            awaitItem().run {
                assertNull(error)
                assertEquals(2, cartItems.size)
            }
        }
    }

    @Test
    fun `given error, when retry click, then show and hide progress`() = runTest {
        getCartProductsUseCase.mock(error = TEST_ERROR, delay = DELAY)
        val sut = createSUT()
        sut.state.test {
            assertTrue { awaitItem().isLoading }
            assertNotNull(awaitItem().error)

            sut.retry()

            assertTrue { awaitItem().isLoading }
            advanceTimeBy(DELAY + 1)
            assertFalse { awaitItem().isLoading }
        }
    }

    @Test
    fun `when clear cart clicked, then confirm dialog shown`() = runTest {
        getCartProductsUseCase.mock()
        val sut = createSUT()
        sut.effects.test {
            expectNoEvents()

            sut.onClearCartBtnClick()

            assertIs<ShowClearCartConfirmationDialog>(expectMostRecentItem())
        }
    }

    @Test
    fun `when clear cart confirmed, then clear cart use case called`() = runTest {
        getCartProductsUseCase.mock()
        val sut = createSUT()
        coVerifyNever { clearCartUseCase.call(Unit) }

        sut.onClearCartClick()

        coVerifyOnce { clearCartUseCase.call(Unit) }
    }

    @Test
    fun `when item deleted from cart, then items updated`() = runTest {
        // todo
    }

    private fun GetCartProductsUseCase.mock(
        delay: Long? = null,
        error: AppError? = null,
        response: List<CartProduct> = ITEMS
    ) =
        every { call(Unit) }.mockFlowResult(
            delayMillis = delay,
            error = error,
            response = response
        )

    private companion object {
        const val DELAY = 2L
        val TEST_ERROR = AppError()
        val ITEMS = listOf(
            CartProduct(
                product = Product(
                    id = 1,
                    name = "name",
                    price = 10.0,
                    thumbnailSrc = "src",
                    quantity = 200,
                    images = emptyList(),
                    description = "desc",
                    productType = ProductType.SIMPLE
                ),
                quantity = 2,
                null
            )
        )
    }
}
