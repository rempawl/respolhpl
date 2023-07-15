package com.example.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.utils.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductImagesViewModelTest : BaseCoroutineTest() {
    private lateinit var viewModel: ProductImagesViewModel
    lateinit var repository: ProductRepository
    lateinit var stateHandle: SavedStateHandle

//    val imgs = FakeData.products.first().images

    @Before
    fun setup() {
        val id = 1
        /*     repository =
                 mock { onBlocking { getProductImages(id) } doReturn flow { emit(Result.Success(imgs)) } }
             stateHandle = mock { on { get<Int>(ProductDetailsFragment.prodId) } doReturn id }
             viewModel = ProductImagesViewModel(repository, stateHandle, ViewPagerPageManagerImpl())*/
    }

    @Test
    fun getImages() {
        /*  coroutineTestRule.runBlockingTest {

              val res = viewModel.result.getOrAwaitValue()
              assertTrue(res.isSuccess)
              assertNotNull(res.checkIfIsSuccessAndListOf<Image>())
              res as Result.Success
              assertThat(res.data, `is`(imgs))
          }*/
    }


}
