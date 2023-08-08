package com.example.respolhpl.home

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.example.respolhpl.data.paging.LoadState
import com.example.respolhpl.data.paging.PagingConfig
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.usecase.GetProductsUseCase
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.utils.BaseCoroutineTest
import com.example.respolhpl.utils.assertLatestItemEquals
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.mockCacheAndFreshWithInputParameters
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue


@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseCoroutineTest() {

    private val getProductsUseCase = mockk<GetProductsUseCase>()

    private fun mockProducts(fetchDelay: Long = TEST_DELAY, isSuccess: Boolean = true) {
        getProductsUseCase.mockCacheAndFreshWithInputParameters(fetchDelay) { param ->
            if (!isSuccess) return@mockCacheAndFreshWithInputParameters DefaultError().left()

            when (param.page) {
                1 -> FakeData.minimalProducts.take(PREFETCH).right()
                2 -> FakeData.minimalProducts.drop(PREFETCH).take(param.perPage).right()
                else -> DefaultError().left()
            }
        }
    }

    private fun createSUT(): HomeViewModel {
        return HomeViewModel(
            getProductsUseCase = getProductsUseCase,
            pagingConfig = PagingConfig(PREFETCH, PAGE_SIZE)
        )
    }


    @Test
    fun `when initialized then correct state set`() = runTest {
        mockProducts()
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
//                assertEquals(FakeData.minimalProducts.take(PREFETCH), items) //todo
            }
            expectNoEvents()
        }
    }

    @Test
    fun `when init fails then error set`() = runTest {
        mockProducts(isSuccess = false)
        createSUT().pagingData.test {
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
        mockProducts()
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
            println("before")
            sut.loadMore()
            println("after")

            awaitItem().run {
                assertIs<LoadState.Loading.LoadingMore>(loadState)
                assertTrue { items.size == PREFETCH }
            }
            expectNoEvents()
            advanceTimeBy(TEST_DELAY + 1)

            expectMostRecentItem().run {
                assertIs<LoadState.Success>(loadState)
                val expectedSize = PREFETCH + PAGE_SIZE
                println("data  ${items.size} $expectedSize $this")

                assertTrue { items.size == expectedSize }
//                assertEquals(FakeData.minimalProducts.take(expectedSize), items) //todo
            }

        }
    }

    /* when item clicked then navigate to detail */

    @Test
    fun `when navigate to destination, then correct state set`() = runTest {
        mockProducts()
        val sut = createSUT()
        println("before")
        sut.shouldNavigate.test {
//            expectNoEvents()
            println("turbine")

            advanceUntilIdle()
            sut.navigate(1)

            assertLatestItemEquals(DestinationId(1))
        }
    }

    companion object {
        const val TEST_DELAY = 2L
        const val PREFETCH = 2
        const val PAGE_SIZE = 1
    }
}
