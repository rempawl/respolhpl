package com.example.respolhpl.home

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.example.respolhpl.data.usecase.GetProductsUseCase
import com.example.respolhpl.fakes.FakeData.minimalProducts
import com.example.respolhpl.home.HomeViewModel.ProductMinimalListItem
import com.example.respolhpl.paging.LoadState
import com.example.respolhpl.paging.PagingConfig
import com.example.respolhpl.utils.BaseCoroutineTest
import com.example.respolhpl.utils.DefaultError
import com.example.respolhpl.utils.assertLatestItemEquals
import com.example.respolhpl.utils.mockFreshWithInputParameters
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseCoroutineTest() {

    private val getProductsUseCase = mockk<GetProductsUseCase>()

    private fun mockProducts(fetchDelay: Long = TEST_DELAY, isSuccess: Boolean = true) {
        getProductsUseCase.mockFreshWithInputParameters(fetchDelay) { param ->
            if (!isSuccess) return@mockFreshWithInputParameters DefaultError().left()
            when (param.page) {
                1 -> minimalProducts.take(PREFETCH).right()
                3 -> minimalProducts.drop(PREFETCH).take(param.perPage).right()
                else -> DefaultError().left()
            }
        }
    }

    private fun createSUT(isSuccess: Boolean = true): HomeViewModel {
        mockProducts(isSuccess = isSuccess)

        return HomeViewModel(
            getProductsUseCase = getProductsUseCase,
            pagingConfig = PagingConfig(PREFETCH, PAGE_SIZE)
        )
    }


    @Test
    fun `when initialized then correct state set`() = runTest {
        createSUT().pagingData.test {
            awaitItem().run {
                assertIs<LoadState.Loading.InitialLoading>(loadState)
                assertTrue { items.isEmpty() }
            }

            expectNoEvents()
            advanceTimeBy(TEST_DELAY + 1)

            expectMostRecentItem().run {
                assertIs<LoadState.Success>(loadState)
                assertTrue { items.size == PREFETCH }
                assertEquals(
                    expected = minimalProducts.take(PREFETCH).map { ProductMinimalListItem(it) },
                    actual = items
                )
            }
            expectNoEvents()
        }
    }

    @Test
    fun `when init fails then error set`() = runTest {
        createSUT(isSuccess = false).pagingData.test {
            awaitItem().run {
                assertIs<LoadState.Loading.InitialLoading>(loadState)
                assertTrue { items.isEmpty() }
            }

            expectNoEvents()
            advanceTimeBy(TEST_DELAY + 1)

            expectMostRecentItem().run {
                assertIs<LoadState.Error>(loadState)
                assertTrue { items.isEmpty() }
            }
        }
    }

    @Test
    fun `given items loaded, when load more triggered then next list loaded`() = runTest {
        val sut = createSUT()
        sut.pagingData.test {
            awaitItem().run {
                assertIs<LoadState.Loading.InitialLoading>(loadState)
                assertTrue { items.isEmpty() }
            }
            expectNoEvents()
            advanceTimeBy(TEST_DELAY + 1)
            expectMostRecentItem().run {
                assertIs<LoadState.Success>(loadState)
                assertTrue { items.size == PREFETCH }
            }
            sut.loadMore()

            awaitItem().run {
                assertIs<LoadState.Loading.LoadingMore>(loadState)
                assertTrue { items.size == PREFETCH }
            }
            expectNoEvents()
            advanceTimeBy(TEST_DELAY + 1)

            expectMostRecentItem().run {
                assertIs<LoadState.Success>(loadState)
                val expectedSize = PREFETCH + PAGE_SIZE
                assertTrue { items.size == expectedSize }
                assertEquals(
                    expected = minimalProducts.take(expectedSize)
                        .map { ProductMinimalListItem(it) },
                    actual = items
                )
            }

        }
    }

    @Test
    fun `when navigate to product details, then correct destination id set`() = runTest {
        val sut = createSUT()
        sut.shouldNavigate.test {
            expectNoEvents()

            sut.navigateToProductDetails(1)

            assertLatestItemEquals(DestinationId(1))
        }
    }

    companion object {
        const val TEST_DELAY = 2L
        const val PREFETCH = 2
        const val PAGE_SIZE = 1
    }
}
