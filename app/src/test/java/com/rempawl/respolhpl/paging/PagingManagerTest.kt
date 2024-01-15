@file:OptIn(ExperimentalCoroutinesApi::class)

package com.rempawl.respolhpl.paging

import app.cash.turbine.test
import com.rempawl.respolhpl.list.paging.LoadState
import com.rempawl.respolhpl.list.paging.PagingConfig
import com.rempawl.respolhpl.list.paging.PagingManager
import com.rempawl.respolhpl.list.paging.PagingParam
import com.rempawl.respolhpl.utils.BaseCoroutineTest
import com.rempawl.respolhpl.utils.DefaultError
import com.rempawl.respolhpl.utils.cancelAndConsumeRemainingItems
import com.rempawl.respolhpl.utils.extensions.EitherResult
import com.rempawl.respolhpl.utils.extensions.lastButOne
import com.rempawl.respolhpl.utils.mockFlowError
import com.rempawl.respolhpl.utils.mockFlowResponse
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
internal class PagingManagerTest : BaseCoroutineTest() {

    private data class TestItem(val id: String)

    private val loadMoreTrigger = MutableSharedFlow<Unit>()

    private val dataSource = mockk<(PagingParam) -> Flow<EitherResult<List<TestItem>>>>()

    private fun createSUT(): PagingManager<TestItem> {
        mockFirstPage()
        mockSecondPage()
        mockThirdPage()

        return PagingManager(
            config = PagingConfig(PREFETCH, LIMIT),
            scope = CoroutineScope(this.testDispatcher),
            loadMoreTrigger = loadMoreTrigger,
            idProducer = { it.id },
            dataSource = { dataSource.invoke(it) }
        )
    }

    @Test
    fun `when init, then prefetch items returned`() = runTest {
        val sut = createSUT()
        sut.pagingData.test {
            awaitItem().run {
                assertIs<LoadState.Loading.InitialLoading>(loadState)
                assertTrue(items.isEmpty())
            }
            advanceUntilIdle()

            expectMostRecentItem().run {
                assertEquals(3, items.size)
                assertEquals("0", items[0].id)
                assertEquals("1", items[1].id)
                assertEquals("2", items[2].id)
                assertIs<LoadState.Success>(loadState)
            }
        }
    }

    @Test
    fun `when load more triggered then second page loaded`() = runTest {
        val sut = createSUT()
        sut.pagingData.test {
            advanceTimeBy(TEST_DELAY + 1)
            loadMoreTrigger.emit(Unit)
            advanceUntilIdle()

            cancelAndConsumeRemainingItems().run {
                lastButOne().run {
                    assertIs<LoadState.Loading.LoadingMore>(loadState)
                    assertEquals(3, items.size)
                }
                last().run {
                    assertIs<LoadState.Success>(loadState)
                    assertEquals(4, items.size)
                    assertEquals("3", items[3].id)
                }
            }
        }
    }

    @Test
    fun `given second page loaded when init triggered then prefetch items returned`() =
        runTest {
            val sut = createSUT()
            sut.pagingData.test {
                advanceUntilIdle()
                loadMoreTrigger.emit(Unit)
                advanceUntilIdle()
                expectMostRecentItem().run {
                    assertIs<LoadState.Success>(loadState)
                    assertEquals(4, items.size)
                    assertEquals("3", items[3].id)
                }

                sut.pagingData.test {
                    advanceUntilIdle()

                    cancelAndConsumeRemainingItems().run {
                        lastButOne().run {
                            assertIs<LoadState.Loading.InitialLoading>(loadState)
                        }
                        last().run {
                            assertEquals(3, items.size)
                            assertEquals("0", items[0].id)
                            assertEquals("1", items[1].id)
                            assertEquals("2", items[2].id)
                        }
                    }
                }
                cancelAndConsumeRemainingItems()
            }
        }

    @Test
    fun `when load more triggered twice then third page loaded`() = runTest {
        val sut = createSUT()

        sut.pagingData.test {

            advanceUntilIdle()
            loadMoreTrigger.emit(Unit)
            advanceUntilIdle()
            assertEquals(4, expectMostRecentItem().items.size)

            loadMoreTrigger.emit(Unit)
            advanceUntilIdle()

            cancelAndConsumeRemainingItems().run {
                lastButOne().run { assertIs<LoadState.Loading.LoadingMore>(loadState) }
                last().run {
                    assertIs<LoadState.Success>(loadState)
                    assertEquals(5, items.size)
                    assertEquals("4", items[4].id)
                }
            }
        }
    }

    @Test
    fun `when load more triggered and init triggered while loading then correct state set`() =
        runTest {
            val sut = createSUT()

            sut.pagingData.test {
                advanceUntilIdle()
                loadMoreTrigger.emit(Unit)
                advanceUntilIdle()
                loadMoreTrigger.emit(Unit)
                advanceTimeBy(TEST_DELAY / 2)
                expectMostRecentItem().run {
                    assertIs<LoadState.Loading.LoadingMore>(loadState)
                    assertEquals(4, items.size)
                }

                sut.pagingData.test {
                    advanceUntilIdle()
                    cancelAndConsumeRemainingItems().run {
                        assertIs<LoadState.Loading.InitialLoading>(lastButOne().loadState)
                        last().run {
                            assertEquals(3, items.size)
                            assertEquals("0", items[0].id)
                            assertEquals("1", items[1].id)
                            assertEquals("2", items[2].id)
                            assertIs<LoadState.Success>(loadState)
                        }
                    }
                }
                cancelAndConsumeRemainingItems()
            }
        }

    @Test
    fun `when error returned, then correct state set`() = runTest {
        val sut = createSUT()
        mockError()
        sut.pagingData.test {
            advanceUntilIdle()
            cancelAndConsumeRemainingItems().run {
                assertIs<LoadState.Loading.InitialLoading>(lastButOne().loadState)
                last().run {
                    assertIs<LoadState.Error.InitError>(loadState)
                    assertTrue(items.isEmpty())
                }
            }
        }
    }

    @Test
    fun `given load error, when retry clicked and loading successful, then prefetch items returned`() =
        runTest {
            val sut = createSUT()
            mockError()
            sut.pagingData.test {
                advanceUntilIdle()
                mockFirstPage()
                assertIs<LoadState.Error.InitError>(expectMostRecentItem().loadState)

                sut.retry()
                advanceUntilIdle()

                cancelAndConsumeRemainingItems().run {
                    lastButOne().run {
                        assertIs<LoadState.Loading.InitialLoading>(loadState)
                        assertTrue(items.isEmpty())
                    }
                    last().run {
                        assertIs<LoadState.Success>(loadState)
                        assertEquals(3, items.size)
                        assertEquals("0", items[0].id)
                        assertEquals("1", items[1].id)
                        assertEquals("2", items[2].id)
                    }
                }
            }
        }

    @Test
    fun `given two pages loaded when refresh clicked then first page loaded`() = runTest {
        val sut = createSUT()
        sut.pagingData.test {
            advanceUntilIdle()
            loadMoreTrigger.emit(Unit)
            advanceUntilIdle()
            assertEquals(4, expectMostRecentItem().items.size)

            sut.refresh()
            advanceUntilIdle()

            cancelAndConsumeRemainingItems().run {
                assertIs<LoadState.Loading.InitialLoading>(lastButOne().loadState)
                last().run {
                    assertIs<LoadState.Success>(loadState)
                    assertEquals(3, items.size)
                    assertEquals("0", items[0].id)
                    assertEquals("1", items[1].id)
                    assertEquals("2", items[2].id)
                }
            }
        }
    }

    @Test
    fun `given load more, when duplicated item returned, then correct state set`() = runTest {
        val sut = createSUT()
        sut.pagingData.test {
            advanceUntilIdle()
            mockSecondPageWithDuplicate()

            loadMoreTrigger.emit(Unit)

            expectMostRecentItem().run {
                assertIs<LoadState.Loading.LoadingMore>(loadState)
                assertEquals(3, items.size)
            }
            advanceUntilIdle()
            expectMostRecentItem().run {
                assertIs<LoadState.Success>(loadState)
                assertEquals(3, items.size)
                assertEquals("2", items[PREFETCH - 1].id)
            }
        }
    }

    private fun mockFirstPage() {
        every { dataSource.invoke(PagingParam(1, PREFETCH)) }.mockFlowResponse(TEST_DELAY) {
            testItems.take(PREFETCH)
        }
    }

    private fun mockSecondPage() {
        every { dataSource.invoke(PagingParam(PREFETCH + 1, LIMIT)) }.mockFlowResponse(TEST_DELAY) {
            testItems.drop(PREFETCH).take(LIMIT)
        }
    }

    private fun mockSecondPageWithDuplicate() {
        every { dataSource.invoke(PagingParam(PREFETCH + 1, LIMIT)) }.mockFlowResponse(TEST_DELAY) {
            testItems.drop(PREFETCH - 1).take(LIMIT)
        }
    }

    private fun mockThirdPage() {
        every { dataSource.invoke(PagingParam(PREFETCH + LIMIT + 1, LIMIT)) }
            .mockFlowResponse(TEST_DELAY) {
                testItems.drop(PREFETCH + LIMIT).take(LIMIT)
            }
    }

    private fun mockError() {
        every { dataSource.invoke(any()) }
            .mockFlowError(TEST_DELAY) { DefaultError() }
    }

    private companion object {
        const val TEST_DELAY = 2L
        const val PREFETCH = 3
        const val LIMIT = 1
        val testItems = (0..10).map { TestItem("$it") }
    }
}
