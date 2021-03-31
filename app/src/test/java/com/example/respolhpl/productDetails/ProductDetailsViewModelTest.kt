package com.example.respolhpl.productDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.FakeData
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.sources.repository.ProductRepositoryImpl
import com.example.respolhpl.getOrAwaitValue
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

    lateinit var repository: ProductRepository
    lateinit var handle: SavedStateHandle
    lateinit var viewModel: ProductDetailsViewModel

    val product = FakeData.resultSuccessProduct.data

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(TestCoroutineDispatcher())

    @Before
    fun setup() {
        repository = mock {
            onBlocking { getProductById(1) } doReturn flow { emit((FakeData.resultSuccessProduct)) }
        }
        handle = mock { on { get<Int>("productId") } doReturn 1 }

        viewModel = ProductDetailsViewModel(handle, repository)
    }

    @Test
    fun resultInit() {
        val res = viewModel.result.getOrAwaitValue()
        verifyBlocking(repository) { getProductById(1) }
        assertThat(res, `is`(FakeData.resultSuccessProduct))
    }


    @Test
    fun onInitMinusBtnIsDisabled() {
        coroutineTestRule.runBlockingTest {
            assertThat(viewModel.isMinusBtnEnabled, `is`(false))
        }
    }

    @Test
    fun onPlusBtnClickIncreasesOrderQuantityDisablesPlusBtnAndEnablesMinusBtn() {
        coroutineTestRule.runBlockingTest {
            assert(product.quantity == 2)
            val qnt = viewModel.orderQuantity
            assertThat(qnt, `is`(1))
            viewModel.onPlusBtnClick()
            assertThat(viewModel.orderQuantity, `is`(2))
            assertThat(viewModel.isPlusBtnEnabled, `is`(false))
            assertThat(viewModel.isMinusBtnEnabled, `is`(true))
        }
    }

    @Test
    fun onPlusBtnClickThenOnMinusBtnClick(){
        coroutineTestRule.runBlockingTest {
            val qnt = viewModel.orderQuantity
            viewModel.onPlusBtnClick()
            viewModel.onMinusBtnClick()
            assertThat(viewModel.orderQuantity,`is`(qnt))
        }
    }

}