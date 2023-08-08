@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.respolhpl.cart.data.sources.paging

import app.cash.turbine.test
import com.example.respolhpl.data.paging.LoadState
import com.example.respolhpl.data.paging.PagingConfig
import com.example.respolhpl.data.paging.PagingManager
import com.example.respolhpl.data.paging.PagingParam
import com.example.respolhpl.utils.BaseCoroutineTest
import com.example.respolhpl.utils.cancelAndConsumeRemainingItems
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.EitherResult
import com.example.respolhpl.utils.extensions.lastButOne
import com.example.respolhpl.utils.mockFlowError
import com.example.respolhpl.utils.mockFlowResponse
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

    private val dataSource = mockk<(PagingParam) -> Flow<EitherResult<List<TestItem>>>> {
        every { this@mockk.invoke(PagingParam(0, PREFETCH)) }.mockFlowResponse(TEST_DELAY) {
            testItems.take(PREFETCH)
        }
        every { this@mockk.invoke(PagingParam(PREFETCH, LIMIT)) }.mockFlowResponse(TEST_DELAY) {
            testItems.drop(PREFETCH).take(LIMIT)
        }
        every { this@mockk.invoke(PagingParam(PREFETCH + LIMIT, LIMIT)) }
            .mockFlowResponse(TEST_DELAY) {
                testItems.drop(PREFETCH + LIMIT).take(LIMIT)
            }
    }

    private fun createSUT(): PagingManager<TestItem> {
        return PagingManager(
            PagingConfig(PREFETCH, LIMIT),
            CoroutineScope(this.testDispatcher),
            loadMoreTrigger
        ) {
            dataSource.invoke(it)
        }
    }

    @Test
    fun `when init called then prefetch items returned`() = runTest {
        val sut = createSUT()
        sut.pagingData.test {

            advanceUntilIdle()

            cancelAndConsumeRemainingItems().run {
                lastButOne().run {
                    assertIs<LoadState.Loading.InitialLoading>(loadState)
                    assertTrue(items.isEmpty())
                }
                last().run {
                    assertEquals(3, items.size)
                    assertEquals("0", items[0].id)
                    assertEquals("1", items[1].id)
                    assertEquals("2", items[2].id)
                    assertIs<LoadState.Success>(loadState)
                }
            }
        }
    }

    @Test
    fun `when init triggered then prefetch items returned`() = runTest {
        val sut = createSUT()
        val emitter = MutableSharedFlow<Unit>()

        sut.pagingData.test {
            expectMostRecentItem().run {
                assertTrue(items.isEmpty())
                assertIs<LoadState.Loading.InitialLoading>(loadState)
            }

            emitter.emit(Unit)
            advanceUntilIdle()

            cancelAndConsumeRemainingItems().run {
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
    fun `when load more triggered then second page loaded`() = runTest {
        val sut = createSUT()
        sut.pagingData.test {
            advanceUntilIdle()

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
    fun `when error returned then correct state set`() = runTest {
        mockError()
        val sut = createSUT()
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
    fun `given load error when retry clicked and loading successful then prefetch items returned`() =
        runTest {
            mockError()
            val sut = createSUT()
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

    private fun mockFirstPage() {
        every { dataSource.invoke(PagingParam(0, PREFETCH)) }.mockFlowResponse(TEST_DELAY) {
            testItems.take(PREFETCH)
        }
    }

    private fun mockError() {
        every { dataSource.invoke(PagingParam(0, PREFETCH)) }
            .mockFlowError(TEST_DELAY) { DefaultError() }
    }

    private companion object {
        const val TEST_DELAY = 2L
        const val PREFETCH = 3
        const val LIMIT = 1
        val testItems = (0..10).map { TestItem("$it") }
    }
}
