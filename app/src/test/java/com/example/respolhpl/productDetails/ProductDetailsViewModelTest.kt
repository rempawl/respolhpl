package com.example.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import arrow.core.right
import com.example.respolhpl.data.usecase.GetProductUseCase
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.utils.BaseCoroutineTest
import com.example.respolhpl.utils.assertLatestItemEquals
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

@ExperimentalCoroutinesApi
class ProductDetailsViewModelTest : BaseCoroutineTest() {
    private val handle: SavedStateHandle =
        mockk<SavedStateHandle> { every { get<Int>(any()) } returns 1 }
    private val useCase =
        mockk<GetProductUseCase> {
            every { cacheAndFresh(any()) } returns flow { emit(FakeData.products.first().right()) }
        }
//            every { get(any()) } returns  FakeData.products.get(0)


    lateinit var viewModel: ProductDetailsViewModel


    fun createSUT() = ProductDetailsViewModel(
        handle,
        getProductUseCase = useCase
    )

    @org.junit.jupiter.api.Test
    fun initalize() = runTest {
        println("test")
        createSUT().product.test {
            advanceUntilIdle()
            println("start")
            assertLatestItemEquals(FakeData.products.first())
        }
    }

    fun resultInit() = {

        /*    val res = viewModel.state.getOrAwaitValue()
            verifyBlocking(productRepository) { getProductById(1) }
            assertThat(res, `is`(FakeData.resultSuccessProduct))
            assertThat(
                viewModel.cartModel.maxQuantity,
                `is`(FakeData.resultSuccessProduct.data.quantity)
            )*/
    }


    fun addTwoItemsToCart() {
        /*  coroutineTestRule.runBlockingTest {
              viewModel.cartModel.currentCartQuantity = 2
              viewModel.onAddToCartClick()
              verifyBlocking(cartRepository) { addProduct(argThat { quantity == 2 }) }
          }*/
    }

    fun navigation() {
        /*viewModel.navigateToFullScreenImage()
        val page = viewModel.shouldNavigate.getOrAwaitValue().getContentIfNotHandled()
        assertThat(page, `is`(0))*/
    }
}
