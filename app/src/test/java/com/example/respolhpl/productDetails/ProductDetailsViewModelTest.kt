package com.example.respolhpl.productDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.FakeCartRepository
import com.example.respolhpl.FakeData
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.getOrAwaitValue
import com.example.respolhpl.productDetails.currentPageState.CurrentViewPagerPageImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@ExperimentalCoroutinesApi
class ProductDetailsViewModelTest {
    lateinit var productRepository: ProductRepository
    lateinit var handle: SavedStateHandle
    lateinit var viewModel: ProductDetailsViewModel
    lateinit var cartRepository: CartRepository

    lateinit var cartModel: CartModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(TestCoroutineDispatcher())

    @Before
    fun setup() {
        productRepository = mock {
            onBlocking { getProductById(1) } doReturn flow { emit((FakeData.resultSuccessProduct)) }
        }
        handle = mock { on { get<Int>(ProductDetailsFragment.prodId) } doReturn 1 }
        cartRepository = spy(FakeCartRepository())
        cartModel = CartModelImpl()

        viewModel = ProductDetailsViewModel(
            handle,
            productRepository,
            cartRepository,
            cartModel,
            CurrentViewPagerPageImpl()
        )
    }

    @Test
    fun resultInit() {
        val res = viewModel.result.getOrAwaitValue()
        verifyBlocking(productRepository) { getProductById(1) }
        assertThat(res, `is`(FakeData.resultSuccessProduct))
        assertThat(
            viewModel.cartModel.maxQuantity,
            `is`(FakeData.resultSuccessProduct.data.quantity)
        )
    }


    @Test
    fun addTwoItemsToCart() {
        coroutineTestRule.runBlockingTest {
            viewModel.cartModel.cartQuantity = 2
            viewModel.onAddToCartClick()
            verifyBlocking(cartRepository) { addProduct(argThat { quantity == 2 }) }
        }
    }

    @Test
    fun navigation() {
        viewModel.navigate()
        val page = viewModel.shouldNavigate.getOrAwaitValue().getContentIfNotHandled()
        assertThat(page, `is`(0))
    }
}