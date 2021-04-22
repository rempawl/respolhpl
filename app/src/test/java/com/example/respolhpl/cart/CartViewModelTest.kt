package com.example.respolhpl.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.respolhpl.*
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.cart.data.sources.CartRepositoryImpl
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {
    lateinit var viewModel: CartViewModel
    lateinit var cartRepository: CartRepository
    private val dispatcherProvider = TestDispatchersProvider()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(dispatcherProvider.test)

    @Before
    fun setup() {


        cartRepository = CartRepositoryImpl(FakeCartProductDao(), dispatcherProvider)
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun init() {
        coroutineTestRule.runBlockingTest {
            val res = viewModel.result.getOrAwaitValue()
            res.checkIfIsSuccessAndListOf<CartProduct>()?.let { prods ->
                assertThat(prods, `is`(FakeData.cartProducts))
            }
        }
    }

    @Test
    fun delete() {
        coroutineTestRule.runBlockingTest {
            //todo
            val prod = FakeData.cartProducts.first()
            viewModel.deleteFromCart(prod)
            viewModel.result.getOrAwaitValue(5).checkIfIsSuccessAndListOf<CartProduct>()
                ?.let { prods ->
                    assertNull(prods.find { prod == it })
                }

        }
    }
}