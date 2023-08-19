package com.example.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.respolhpl.data.model.domain.Images
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManagerImpl
import com.example.respolhpl.utils.BaseCoroutineTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

@ExperimentalCoroutinesApi
class ProductImagesViewModelTest : BaseCoroutineTest() {

    private val stateHandle: SavedStateHandle = mockk()

    private fun createSUT(images: Images? = TEST_IMAGES): ProductImagesViewModel {
        mockSavedStateHandle(images)
        return ProductImagesViewModel(stateHandle, ViewPagerPageManagerImpl())
    }

    private fun mockSavedStateHandle(images: Images? = TEST_IMAGES) {
        every { stateHandle.get<Images>(any()) } returns images
    }

    @Test
    fun `when init, then images set`() = runTest {
        createSUT().state.test {

            assertEquals(TEST_IMAGES, expectMostRecentItem().images)
            expectNoEvents()
        }
    }

    @Test
    fun `when init fails, then error set`() = runTest {
        createSUT(images = null).state.test {

            expectMostRecentItem().run {
                assertNotNull(this.error)
                assertIs<NullProductIdError>(error!!)
            }
            expectNoEvents()
        }
    }

    private companion object {
        val TEST_IMAGES = Images(FakeData.products.first().images)
    }
}

