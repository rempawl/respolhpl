package com.example.respolhpl.home

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.example.respolhpl.data.usecase.GetProductsUseCase
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.usecase.ActionFlowResultUseCase
import com.example.respolhpl.data.usecase.ActionResultUseCase
import com.example.respolhpl.data.usecase.AsyncUseCase
import com.example.respolhpl.data.usecase.FlowResultUseCase
import com.example.respolhpl.data.usecase.FlowUseCase
import com.example.respolhpl.data.usecase.ResultUseCase
import com.example.respolhpl.data.usecase.StoreUseCase
import com.example.respolhpl.data.usecase.UseCase
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.data.paging.LoadState
import com.example.respolhpl.data.paging.PagingConfig
import com.example.respolhpl.utils.BaseCoroutineTest
import com.example.respolhpl.utils.assertLatestItemEquals
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.EitherResult
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

inline fun <reified P : Any, R, T : AsyncUseCase<P, R>> T.mock(
    value: R,
    param: P? = null,
    delayMillis: Long = 0,
) = coEvery { this@mock.invoke(param ?: any()) } coAnswers {
    if (delayMillis > 0) delay(delayMillis)
    value
}

inline fun <reified P : Any, R, T : AsyncUseCase<P, R>> T.mockWithInputParameters(
    delayMillis: Long = 0,
    crossinline valueFactory: (P) -> R,
) {
    val argument = slot<P>()
    coEvery { this@mockWithInputParameters.invoke(capture(argument)) } coAnswers {
        if (delayMillis > 0) delay(delayMillis)
        valueFactory(argument.captured)
    }
}

inline fun <reified Param : Any, Result, T : StoreUseCase<Param, Result>> T.mockCacheAndFreshWithInputParameters(
    delayMillis: Long = 0,
    crossinline valueFactory: (Param) -> EitherResult<Result>
) {
    val argument = slot<Param>()
    coEvery { this@mockCacheAndFreshWithInputParameters.cacheAndFresh(capture(argument)) } coAnswers {
        flow {
            delay(delayMillis)
            emit(valueFactory(argument.captured))
        }
    }
}

suspend operator fun <Output> AsyncUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> AsyncUseCase<Param, Output>.invoke(param: Param) =
    call(param)

suspend operator fun <Output> ResultUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> ResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)

suspend operator fun <Output> ActionResultUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> ActionResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)

operator fun <Output> UseCase<Unit, Output>.invoke() = call(Unit)
operator fun <Param, Output> UseCase<Param, Output>.invoke(param: Param) =
    call(param)

suspend operator fun <Output> FlowUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> FlowUseCase<Param, Output>.invoke(param: Param) =
    call(param)

suspend operator fun <Output> FlowResultUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> FlowResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)

suspend operator fun <Output> ActionFlowResultUseCase<Unit, Output>.invoke() = call(Unit)
suspend operator fun <Param, Output> ActionFlowResultUseCase<Param, Output>.invoke(param: Param) =
    call(param)

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseCoroutineTest() {
    val repository: ProductRepository = mockk()


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
                assertEquals(FakeData.minimalProducts.take(PREFETCH), items)
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
                assertEquals(FakeData.minimalProducts.take(expectedSize), items)
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
