package com.example.respolhpl.productDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.respolhpl.CoroutineTestRule
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking

@ExperimentalCoroutinesApi
class ProductDetailsViewModelTest {

    lateinit var productRepository: ProductRepository
    lateinit var handle: SavedStateHandle
    lateinit var viewModel: ProductDetailsViewModel
    lateinit var cartRepository: CartRepository

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
        cartRepository = mock {}

        viewModel = ProductDetailsViewModel(
            handle,
            productRepository,
            cartRepository,
            CurrentViewPagerPageImpl()
        )
    }

    @Test
    fun resultInit() {
        val res = viewModel.result.getOrAwaitValue()
        verifyBlocking(productRepository) { getProductById(1) }
        assertThat(res, `is`(FakeData.resultSuccessProduct))
    }


    @Test
    fun onInitMinusBtnIsDisabled() {
        coroutineTestRule.runBlockingTest {
            assertThat(viewModel.isMinusBtnEnabled, `is`(false))
        }
    }

    @Test
    fun whenMaxQuantityIs2onPlusBtnClickIncreasesOrderQuantityDisablesPlusBtnAndEnablesMinusBtn() {
        coroutineTestRule.runBlockingTest {
            val product = FakeData.resultSuccessProduct.data
            assert(product.quantity == 2)

            val qnt = viewModel.cartQuantity
            assertThat(qnt, `is`(1))
            viewModel.onPlusBtnClick()

            assertThat(viewModel.cartQuantity, `is`(2))
            assertThat(viewModel.isPlusBtnEnabled, `is`(false))
            assertThat(viewModel.isMinusBtnEnabled, `is`(true))
        }
    }

    @Test
    fun onPlusBtnClickThenOnMinusBtnClick() {
        coroutineTestRule.runBlockingTest {
            val qnt = viewModel.cartQuantity
            viewModel.onPlusBtnClick()
            viewModel.onMinusBtnClick()
            assertThat(viewModel.cartQuantity, `is`(qnt))
        }
    }

}