@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.respolhpl

import app.cash.turbine.test
import com.example.respolhpl.utils.BaseCoroutineTest
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MainActivityViewModelTest : BaseCoroutineTest() {
    private val isConnectedFlow = MutableSharedFlow<Boolean>()

    private fun createSut() = MainActivityViewModel(
        networkListener = mockk {
            every { isConnected } returns isConnectedFlow
        }
    )

    @Test
    fun `when isConnected to internet then correct state set`() = runTest {
        createSut().isConnected.test {
            expectNoEvents()
            isConnectedFlow.emit(true)

            assertTrue(awaitItem())
        }
    }

    @Test
    fun `given connected when lost connection then correct state`() = runTest {
        createSut().isConnected.test {
            expectNoEvents()
            isConnectedFlow.emit(true)
            assertTrue(awaitItem())

            isConnectedFlow.emit(false)
            assertFalse(awaitItem())
        }
    }

    @Test
    fun `given disconnected when connection restored then correct state`() = runTest {
        createSut().isConnected.test {
            expectNoEvents()
            isConnectedFlow.emit(false)
            assertFalse(awaitItem())

            isConnectedFlow.emit(true)
            assertTrue(awaitItem())
        }
    }

}