package com.example.respolhpl.productDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.getOrAwaitValue
import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManagerImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ProductImagesViewModelTest {
    private lateinit var viewModel: ProductImagesViewModel
    lateinit var repository: ProductRepository
    lateinit var stateHandle: SavedStateHandle

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(TestCoroutineDispatcher())

    val imgs = FakeData.products.first().images

    @Before
    fun setup() {
        val id = 1
        repository =
            mock { onBlocking { getProductImages(id) } doReturn flow { emit(Result.Success(imgs)) } }
        stateHandle = mock { on { get<Int>(ProductDetailsFragment.prodId) } doReturn id }
        viewModel = ProductImagesViewModel(repository, stateHandle, ViewPagerPageManagerImpl())
    }

    @Test
    fun getImages() {
        coroutineTestRule.runBlockingTest {

            val res = viewModel.result.getOrAwaitValue()
            assertTrue(res.isSuccess)
            assertNotNull(res.checkIfIsSuccessAndListOf<Image>())
            res as Result.Success
            assertThat(res.data, `is`(imgs))
        }
    }


}
