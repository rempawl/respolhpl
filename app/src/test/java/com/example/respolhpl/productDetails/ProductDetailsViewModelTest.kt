package com.example.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import app.cash.turbine.turbineScope
import com.example.respolhpl.data.usecase.GetProductUseCase
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.utils.BaseCoroutineTest
import com.example.respolhpl.utils.assertLatestItemEquals
import com.example.respolhpl.utils.coVerifyOnce
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.mockCacheAndFresh
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
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

    private val getProductUseCase = mockk<GetProductUseCase>()
    private val quantityTextFlow = MutableSharedFlow<CharSequence>()

    private fun createSUT(error: DefaultError? = null): ProductDetailsViewModel {
        mockProduct(error)
        return ProductDetailsViewModel(
            handle,
            getProductUseCase = getProductUseCase
        )
    }

    private fun mockProduct(error: DefaultError? = null) {
        getProductUseCase.mockCacheAndFresh(
            delayMillis = TEST_DELAY,
            error = error,
            value = FakeData.products.first()
        )
    }


    @Test
    fun `when init successful, then correct state set`() = runTest {
        createSUT().product.test {
            expectNoEvents()

            advanceTimeBy(TEST_DELAY + 1)

            assertLatestItemEquals(FakeData.products.first())
            coVerifyOnce { getProductUseCase.cacheAndFresh(any()) }
        }
    }

    @Test
    fun `when init, then loading shown and hidden`() = runTest {
        createSUT().state.test {
            assertTrue { awaitItem().isLoading }

            advanceTimeBy(TEST_DELAY + 1)

            assertFalse { awaitItem().isLoading }
            expectNoEvents()
        }
    }

    @Test
    fun `when init fails, then error set`() = runTest {
        createSUT(DefaultError()).state.test {
            assertNull(awaitItem().error)

            advanceTimeBy(TEST_DELAY + 1)

            assertNotNull(awaitItem().error)
            coVerifyOnce { getProductUseCase.cacheAndFresh(any()) }
        }
    }

    @Test
    fun `when text changed, then correct state set`() = runTest {
        turbineScope {
            val sut = createSUT()
            val quantity = sut.cartQuantity.testIn(backgroundScope)
            val isMinusBtnEnabled = sut.isMinusBtnEnabled.testIn(backgroundScope)
            val isPlusBtnEnabled = sut.isPlusBtnEnabled.testIn(backgroundScope)

            advanceUntilIdle()
            assertEquals(1, quantity.expectMostRecentItem())
            assertFalse { isMinusBtnEnabled.expectMostRecentItem() }
            assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
            sut.setQuantityChangedListener(quantityTextFlow)

            quantityTextFlow.emit("22")
            advanceUntilIdle()

            assertEquals(22, quantity.expectMostRecentItem())
            assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
            assertTrue { isMinusBtnEnabled.expectMostRecentItem() }
        }
    }

    @Test
    fun `when text changed over max quantity,then quantity equals max quantity and plus btn disabled`() =
        runTest {
            turbineScope {
                val sut = createSUT()
                val quantity = sut.cartQuantity.testIn(backgroundScope)
                val isPlusBtnEnabled = sut.isPlusBtnEnabled.testIn(backgroundScope)
                advanceUntilIdle()
                assertEquals(1, quantity.expectMostRecentItem())
                assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
                sut.setQuantityChangedListener(quantityTextFlow)

                quantityTextFlow.emit("${TEST_PRODUCT.quantity + 10}")
                advanceUntilIdle()

                assertEquals(TEST_PRODUCT.quantity, quantity.expectMostRecentItem())
                assertFalse { isPlusBtnEnabled.expectMostRecentItem() }
            }
        }



    @Test
    fun `when initialized, then correct quantity, plus btn enabled and minus btn disabled`() =
        runTest {
            turbineScope {
                val sut = createSUT()

                val quantity = sut.cartQuantity.testIn(backgroundScope)
                val isMinusBtnEnabled = sut.isMinusBtnEnabled.testIn(backgroundScope)
                val isPlusBtnEnabled = sut.isPlusBtnEnabled.testIn(backgroundScope)

                advanceUntilIdle()
                assertEquals(1, quantity.expectMostRecentItem())
                assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
                assertFalse { isMinusBtnEnabled.expectMostRecentItem() }
            }
        }


    @Test
    fun `given quantity equals one, when plus btn clicked, then quantity equals two and plus and minus btns enabled`() =
        runTest {
            turbineScope {
                val sut = createSUT()
                val quantity = sut.cartQuantity.testIn(backgroundScope)
                val isMinusBtnEnabled = sut.isMinusBtnEnabled.testIn(backgroundScope)
                val isPlusBtnEnabled = sut.isPlusBtnEnabled.testIn(backgroundScope)
                advanceUntilIdle()
                assertEquals(1, quantity.expectMostRecentItem())
                assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
                assertFalse { isMinusBtnEnabled.expectMostRecentItem() }

                sut.onPlusBtnClick()

                assertEquals(2, quantity.expectMostRecentItem())
                assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
                assertTrue { isMinusBtnEnabled.expectMostRecentItem() }
            }
        }

    @Test
    fun `given quantity equals one, when plus btn and minus btn are clicked, then quantity equals one, plus btn is enabled and minus btn is disabled`() =
        runTest {
            turbineScope {
                val sut = createSUT()
                val quantity = sut.cartQuantity.testIn(backgroundScope)
                val isMinusBtnEnabled = sut.isMinusBtnEnabled.testIn(backgroundScope)
                val isPlusBtnEnabled = sut.isPlusBtnEnabled.testIn(backgroundScope)
                advanceUntilIdle()
                sut.onPlusBtnClick()
                assertEquals(2, quantity.expectMostRecentItem())
                assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
                assertTrue { isMinusBtnEnabled.expectMostRecentItem() }

                sut.onMinusBtnClick()

                assertEquals(1, quantity.expectMostRecentItem())
                assertTrue { isPlusBtnEnabled.expectMostRecentItem() }
                assertFalse { isMinusBtnEnabled.expectMostRecentItem() }
            }
        }

    private companion object {
        const val TEST_DELAY = 2L
        val TEST_PRODUCT = FakeData.products.first()
    }
}
