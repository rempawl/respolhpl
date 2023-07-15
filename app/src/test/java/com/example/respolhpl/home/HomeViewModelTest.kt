package com.example.respolhpl.home

import app.cash.turbine.test
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.utils.BaseCoroutineTest
import com.example.respolhpl.utils.assertLatestItemEquals
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest : BaseCoroutineTest() {
    val repository: ProductRepository = mockk()


    private fun createSUT(): HomeViewModel {
        coEvery { repository.getProducts() } coAnswers {
//            delay(TEST_DELAY)
//            flow { emit(PagingData.from(FakeData.minimalProducts)) }
            emptyFlow()
        }
        return HomeViewModel(repository)
    }


    @Test
    fun `when initialized then correct state set`() = runTest {
        createSUT().items.test {
//            expectNoEvents()

            advanceTimeBy(TEST_DELAY)

            val x = expectMostRecentItem()
//            assertLatestItemEquals(PagingData.from(FakeData.minimalProducts))
            cancel()
        }
    }

    @Test
    fun `when navigate to destination, then correct state set`() = runTest {
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
        const val TEST_DELAY = 100L
    }
}
