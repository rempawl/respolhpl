package com.example.respolhpl.cart

import com.example.respolhpl.utils.BaseCoroutineTest
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
class CartViewModelTest : BaseCoroutineTest() {

    private fun createSUT() = CartViewModel()

    // todo
    @Test
    fun `when initialized, then state set`() = runTest {
        createSUT()
    }

    @Test
    fun `when init, then show and hide progress`() = runTest {}

    @Test
    fun `when init fails, then error is displayed`() = runTest {}

    @Test
    fun `when item deleted from cart, then items updated`() = runTest {}

}
