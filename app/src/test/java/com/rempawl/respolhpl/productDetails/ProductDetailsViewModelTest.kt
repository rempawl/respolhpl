package com.rempawl.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.rempawl.respolhpl.data.model.domain.ProductImage
import com.rempawl.respolhpl.data.model.domain.Images
import com.rempawl.respolhpl.data.model.domain.details.ProductAttribute
import com.rempawl.respolhpl.data.model.domain.details.ProductDetails
import com.rempawl.respolhpl.data.model.domain.details.ProductVariant
import com.rempawl.respolhpl.data.usecase.AddToCartUseCase
import com.rempawl.respolhpl.data.usecase.GetProductDetailsUseCase
import com.rempawl.respolhpl.fakes.FakeData
import com.rempawl.respolhpl.utils.BaseCoroutineTest
import com.rempawl.respolhpl.utils.DefaultError
import com.rempawl.respolhpl.utils.HtmlParser
import com.rempawl.respolhpl.utils.coVerifyNever
import com.rempawl.respolhpl.utils.coVerifyOnce
import com.rempawl.respolhpl.utils.mockFlowResult
import com.rempawl.respolhpl.utils.mockFlowResultUseCase
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class ProductDetailsViewModelTest : BaseCoroutineTest() {

    private val handle: SavedStateHandle = mockk { every { get<Int>(any()) } returns 1 }
    private val getProductUseCase = mockk<GetProductDetailsUseCase>()
    private val addToCartUseCase = mockk<AddToCartUseCase>()
    private val htmlParser = mockk<HtmlParser> {
        every { parse(any()) } returns "description"
    }

    private fun createSUT(
        productError: DefaultError? = null,
        delay: Long = 0L,
        product: ProductDetails = TEST_PRODUCT,
    ): ProductDetailsViewModel {
        mockProduct(product = product, error = productError, delay = delay)

        return ProductDetailsViewModel(
            savedStateHandle = handle,
            getProductDetailsUseCase = getProductUseCase,
            addToCartUseCase = addToCartUseCase,
            productDetailsFormatter = ProductDetailsFormatter(htmlParser)
        )
    }

    @Test
    fun `given simple product, when init successful, then correct state set`() = runTest {
        val sut = createSUT(product = TEST_PRODUCT.copy(variants = emptyList()))
        sut.state.test {
            expectMostRecentItem().run {
                assertEquals(
                    expected = ProductDetailsState(
                        productQuantity = 60,
                        variants = emptyList(),
                        currentVariant = null,
                        descriptionFormatted = "description",
                        productName = "Product",
                        toolbarLabel = "Product",
                        priceFormatted = "42.99 PLN",
                        images = Images(
                            listOf(
                                ProductImage(
                                    "testUri1",
                                    14
                                ),
                                ProductImage(
                                    "testUrl2",
                                    13
                                )
                            )
                        ),
                        showProgress = false,
                        cartQuantity = 0,
                        productError = null,
                        showVariantPicker = false
                    ),
                    actual = this
                )
                assertEquals(0, cartQuantity)
                assertFalse { isVariantCardVisible }
            }
            coVerifyOnce { getProductUseCase.call(any()) }
        }
    }

    @Test
    fun `given variable product, when init successful, then correct state set`() = runTest {
        createSUT(delay = TEST_DELAY).state.test {
            advanceTimeBy(TEST_DELAY + 1)
            expectMostRecentItem().run {
                assertEquals(
                    expected = ProductDetailsState(
                        productQuantity = 60,
                        variants = listOf(
                            VariantItem(
                                id = 111,
                                quantity = 30,
                                price = 10.0,
                                attributesFormatted = "size: small"
                            ),
                            VariantItem(
                                id = 112,
                                quantity = 40,
                                price = 20.0,
                                attributesFormatted = "size: medium"
                            )
                        ),
                        currentVariant = VariantItem(
                            id = 111,
                            quantity = 30,
                            price = 10.0,
                            attributesFormatted = "size: small"
                        ),
                        descriptionFormatted = "description",
                        productName = "Product",
                        toolbarLabel = "Product",
                        priceFormatted = "10.0 PLN",
                        images = Images(
                            listOf(
                                ProductImage(
                                    "testUri1",
                                    14
                                ),
                                ProductImage(
                                    "testUrl2",
                                    13
                                )
                            )
                        ),
                        showProgress = false,
                        cartQuantity = 0,
                        productError = null,
                        showVariantPicker = false
                    ),
                    actual = this
                )
                assertEquals(0, cartQuantity)
            }
            coVerifyOnce { getProductUseCase.call(any()) }
        }
    }

    @Test
    fun `when init, then loading shown and hidden`() = runTest {
        createSUT(delay = TEST_DELAY).state.test {
            assertTrue { awaitItem().showProgress }

            advanceTimeBy(TEST_DELAY + 1)

            assertFalse { awaitItem().showProgress }
            expectNoEvents()
        }
    }

    @Test
    fun `when init fails, then error set`() = runTest {
        createSUT(DefaultError(), TEST_DELAY).state.test {
            assertNull(awaitItem().productError)

            advanceTimeBy(TEST_DELAY + 1)

            assertNotNull(awaitItem().productError)
            coVerifyOnce { getProductUseCase.call(any()) }
        }
    }

    @Test
    fun `given init fails, when retry clicked and request succeeds, then correct state set`() =
        runTest {
            val sut = createSUT(DefaultError(), TEST_DELAY)
            sut.state.test {
                assertNull(awaitItem().productError)
                advanceTimeBy(TEST_DELAY + 1)
                assertNotNull(awaitItem().productError)
                coVerifyOnce { getProductUseCase.call(any()) }

                mockProduct(product = TEST_PRODUCT, delay = TEST_DELAY)
                sut.retry()
                advanceTimeBy(TEST_DELAY + 1)

                expectMostRecentItem().run {
                    assertEquals(
                        expected = ProductDetailsState(
                            productQuantity = 60,
                            variants = listOf(
                                VariantItem(
                                    id = 111,
                                    quantity = 30,
                                    price = 10.0,
                                    attributesFormatted = "size: small"
                                ),
                                VariantItem(
                                    id = 112,
                                    quantity = 40,
                                    price = 20.0,
                                    attributesFormatted = "size: medium"
                                )
                            ),
                            currentVariant = VariantItem(
                                id = 111,
                                quantity = 30,
                                price = 10.0,
                                attributesFormatted = "size: small"
                            ),
                            descriptionFormatted = "description",
                            productName = "Product",
                            toolbarLabel = "Product",
                            priceFormatted = "10.0 PLN",
                            images = Images(
                                listOf(
                                    ProductImage(
                                        "testUri1",
                                        14
                                    ),
                                    ProductImage(
                                        "testUrl2",
                                        13
                                    )
                                )
                            ),
                            showProgress = false,
                            cartQuantity = 0,
                            productError = null,
                            showVariantPicker = false
                        ),
                        actual = this
                    )
                }
                coVerify(exactly = 2) { getProductUseCase.call(any()) }
            }
        }

    @Test
    fun `when quantity text changed, then correct state set`() = runTest {
        val sut = createSUT()
        sut.state.test {
            expectMostRecentItem().run {
                assertEquals(0, cartQuantity)
                assertFalse { isMinusBtnEnabled }
                assertTrue { isPlusBtnEnabled }
            }

            sut.onQuantityChanged("22")
            advanceUntilIdle()

            awaitItem().run {
                assertEquals(22, cartQuantity)
                assertTrue { isPlusBtnEnabled }
                assertTrue { isMinusBtnEnabled }
            }
        }
    }

    @Test
    fun `when quantity text changed over max quantity,then quantity equals max quantity and plus btn disabled`() =
        runTest {
            val sut = createSUT()
            sut.state.test {
                expectMostRecentItem().run {
                    assertEquals(0, cartQuantity)
                    assertTrue { isPlusBtnEnabled }
                }
                sut.onQuantityChanged("120")
                advanceUntilIdle()

                awaitItem().run {
                    assertEquals(30, cartQuantity)
                    assertFalse { isPlusBtnEnabled }
                }
            }
        }

    @Test
    fun `when quantity text changed to empty, then cart quantity is 0`() = runTest {
        val sut = createSUT()
        sut.state.test {
            sut.onQuantityChanged("10")
            expectMostRecentItem().run {
                assertEquals(10, cartQuantity)
            }

            sut.onQuantityChanged("")
            advanceUntilIdle()

            awaitItem().run {
                assertEquals(0, cartQuantity)
            }
        }
    }

    @Test
    fun `when quantity text changed to blank, then cart quantity is 0`() = runTest {
        val sut = createSUT()
        sut.state.test {
            sut.onQuantityChanged("10")
            expectMostRecentItem().run {
                assertEquals(10, cartQuantity)
            }

            sut.onQuantityChanged("  ")
            advanceUntilIdle()

            awaitItem().run {
                assertEquals(0, cartQuantity)
            }
        }
    }

    @Test
    fun ` when initialized, then correct quantity plus btn is enabled and minus btn is disabled`() =
        runTest {
            createSUT().state.test {
                expectMostRecentItem().run {
                    assertEquals(0, cartQuantity)
                    assertTrue { isPlusBtnEnabled }
                    assertFalse { isMinusBtnEnabled }
                }
            }
        }


    @Test
    fun `given quantity equals one, when plus btn clicked, then quantity equals two and plus and minus btns enabled`() =
        runTest {
            val sut = createSUT()
            sut.state.test {
                sut.onQuantityChanged("1")
                expectMostRecentItem().run {
                    assertEquals(1, cartQuantity)
                    assertTrue { isPlusBtnEnabled }
                    assertFalse { isMinusBtnEnabled }
                }

                sut.onPlusBtnClick()

                awaitItem().run {
                    assertEquals(2, cartQuantity)
                    assertTrue { isPlusBtnEnabled }
                    assertTrue { isMinusBtnEnabled }
                }
            }
        }

    @Test
    fun `given initialized, when plus btn and minus btn are clicked, then quantity equals zero, plus btn is enabled and minus btn is disabled`() =
        runTest {
            val sut = createSUT()
            sut.state.test {
                sut.onPlusBtnClick()
                advanceUntilIdle()

                expectMostRecentItem().run {
                    assertEquals(1, cartQuantity)
                    assertTrue { isPlusBtnEnabled }
                    assertFalse { isMinusBtnEnabled }
                }
                sut.onMinusBtnClick()

                awaitItem().run {
                    assertEquals(0, cartQuantity)
                    assertTrue { isPlusBtnEnabled }
                    assertFalse { isMinusBtnEnabled }
                }
            }
        }


    @Test
    fun `when add to cart click, then show and hide progress`() = runTest {
        val sut = createSUT()
        mockAddToCart()
        sut.state.test {
            assertFalse { expectMostRecentItem().showProgress }

            sut.onAddToCartClick()

            assertTrue { awaitItem().showProgress }
            advanceTimeBy(TEST_DELAY + 1)
            assertFalse { awaitItem().showProgress }
        }
    }

    @Test
    fun `given quantity equals 2, when add to cart clicked and succeeds, then 2 items added to cart and cart quantity is 0`() =
        runTest {
            turbineScope {
                val sut = createSUT()
                mockAddToCart()
                val state = sut.state.testIn(backgroundScope)
                val effects = sut.effects.testIn(backgroundScope)
                sut.onQuantityChanged("2")
                assertEquals(2, state.expectMostRecentItem().cartQuantity)
                coVerifyNever { addToCartUseCase.call(any()) }

                sut.onAddToCartClick()
                advanceTimeBy(TEST_DELAY + 1)

                assertEquals(ProductDetailsEffect.ItemAddedToCart(2), effects.awaitItem())
                assertEquals(0, state.expectMostRecentItem().cartQuantity)
                coVerifyOnce { addToCartUseCase.call(AddToCartUseCase.Param(1, 2)) }
            }
        }

    @Test
    fun `when add to cart fails, then error is displayed`() = runTest {
        val sut = createSUT()
        val error = DefaultError(message = "error occurred when adding to cart")
        mockAddToCart(error)

        sut.showError.test {
            expectNoEvents()
            coVerifyNever { addToCartUseCase.call(any()) }

            sut.onAddToCartClick()
            advanceTimeBy(TEST_DELAY + 1)

            awaitItem().run {
                assertNotNull(this)
                assertEquals(error, this)
            }
            coVerifyOnce { addToCartUseCase.call(any()) }
        }
    }

    @Test
    fun `when pick variant btn click, then show variant picker is true`() = runTest {
        val sut = createSUT()
        sut.state.test {
            assertFalse { awaitItem().showVariantPicker }

            sut.onPickVariationBtnClick()

            assertTrue { awaitItem().showVariantPicker }
        }
    }

    @Test
    fun `when close variant picker called, then show variant picker is false`() = runTest {
        val sut = createSUT()
        sut.state.test {
            sut.onPickVariationBtnClick()
            assertTrue { expectMostRecentItem().showVariantPicker }

            sut.onCloseVariantPicker()

            assertFalse { awaitItem().showVariantPicker }
        }
    }

    @Test
    fun `when variant selected, then current variant updated`() = runTest {
        val sut = createSUT()
        sut.state.test {
            assertEquals(
                expected = VariantItem(
                    id = 111,
                    quantity = 30,
                    price = 10.0,
                    attributesFormatted = "size: small"
                ),
                actual = expectMostRecentItem().currentVariant
            )

            sut.onVariantClicked(VariantItem(1, 10, 10.0, "size: big"))

            assertEquals(
                VariantItem(1, 10, 10.0, "size: big"),
                awaitItem().currentVariant
            )
        }
    }

    @Test
    fun `when variant changed, then max quantity updated`() = runTest {
        val sut = createSUT()
        sut.state.test {
            expectMostRecentItem().run {
                assertEquals(
                    expected = VariantItem(
                        id = 111,
                        quantity = 30,
                        price = 10.0,
                        attributesFormatted = "size: small"
                    ),
                    actual = currentVariant
                )
                assertEquals(30, maxQuantity)
            }

            sut.onVariantClicked(VariantItem(1, 10, 10.0, "size: big"))

            awaitItem().run {
                assertEquals(
                    VariantItem(1, 10, 10.0, "size: big"),
                    currentVariant
                )

                assertEquals(10, maxQuantity)
            }
        }
    }

    @Test
    fun `when variant changed and quantity is greater than variant quantity, then quantity equals variant quantity and maxQuantity changed`() =
        runTest {
            val sut = createSUT()
            sut.state.test {
                sut.onQuantityChanged("20")
                expectMostRecentItem().run {
                    assertEquals(20, cartQuantity)
                    assertEquals(30, maxQuantity)
                }

                sut.onVariantClicked(VariantItem(1, 10, 10.0, "size: big"))

                awaitItem().run {
                    assertEquals(10, cartQuantity)
                    assertEquals(10, maxQuantity)
                }
            }
        }

    @Test
    fun `when variant picked, then show variant picker is false`() = runTest {
        val sut = createSUT()
        sut.state.test {
            sut.onPickVariationBtnClick()
            assertTrue { expectMostRecentItem().showVariantPicker }

            sut.onVariantClicked(VariantItem(1, 10, 10.0, "size: big"))

            assertFalse { awaitItem().showVariantPicker }
        }
    }

    @Test
    fun `when image clicked, then NavigateToFullScreenImage effect set`() = runTest {
        val sut = createSUT()
        sut.effects.test {
            expectNoEvents()

            sut.onImageClicked()

            assertEquals(
                ProductDetailsEffect.NavigateToFullScreenImage(sut.stateValue.images),
                awaitItem()
            )
        }
    }


    private fun mockProduct(product: ProductDetails, error: DefaultError? = null, delay: Long) {
        mockFlowResultUseCase(
            delayMillis = delay,
            error = error,
            response = product,
            useCaseBlock = { getProductUseCase.call(any()) }
        )
    }

    private fun mockAddToCart(error: DefaultError? = null) {
        every { addToCartUseCase.call(any()) }.mockFlowResult(
            delayMillis = TEST_DELAY,
            response = Unit,
            error = error
        )

    }

    private companion object {
        const val TEST_DELAY = 2L
        val TEST_PRODUCT =
            ProductDetails(
                product = FakeData.products.first(),
                variants = listOf(
                    ProductVariant(
                        id = 111,
                        price = 10.0,
                        quantity = 30,
                        productAttributes = listOf(ProductAttribute(name = "size", value = "small"))
                    ),
                    ProductVariant(
                        id = 112,
                        price = 20.0,
                        quantity = 40,
                        productAttributes = listOf(
                            ProductAttribute(
                                name = "size",
                                value = "medium"
                            )
                        )
                    )
                )
            )
    }
}
