package com.example.respolhpl.productDetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.FakeData
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.getOrAwaitValue
import com.example.respolhpl.utils.event.Event
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartModelImplTest {
    lateinit var cartModelImpl: CartModelImpl

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(TestDispatchersProvider().test)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Before
    fun setup() {
        cartModelImpl = CartModelImpl()
    }

    @Test
    fun onInitMinusBtnIsDisabled() {
        coroutineTestRule.runBlockingTest {
            assertThat(cartModelImpl.isMinusBtnEnabled, `is`(false))
        }
    }

    @Test
    fun onPlusBtnClickThenDisablePlusBtnAndEnableMinusBtn() {
        coroutineTestRule.runBlockingTest {
            cartModelImpl.maxQuantity = 2
            cartModelImpl.cartQuantity = 1
            cartModelImpl.onPlusBtnClick()

            assertThat(cartModelImpl.cartQuantity, `is`(2))
            assertThat(cartModelImpl.isPlusBtnEnabled, `is`(false))
            assertThat(cartModelImpl.isMinusBtnEnabled, `is`(true))
        }
    }

    @Test
    fun onMinusBtnClickThenDisableMinusBtn(){
        cartModelImpl.maxQuantity =2
        cartModelImpl.cartQuantity =2
        assertFalse(cartModelImpl.isPlusBtnEnabled)
        assertTrue(cartModelImpl.isMinusBtnEnabled)

        cartModelImpl.onMinusBtnClick()

        assertTrue(cartModelImpl.isPlusBtnEnabled)
        assertFalse(cartModelImpl.isMinusBtnEnabled)
        assertThat(cartModelImpl.cartQuantity,`is`(1))

    }

    @Test
    fun onPlusBtnClickThenOnMinusBtnClick() {
        coroutineTestRule.runBlockingTest {
            cartModelImpl.maxQuantity = 6
            val qnt = cartModelImpl.cartQuantity
            cartModelImpl.onPlusBtnClick()
            cartModelImpl.onMinusBtnClick()
            assertThat(cartModelImpl.cartQuantity, `is`(qnt))
        }
    }

    @Test
    fun createCartProductWhenCartQuantityIs5(){
        coroutineTestRule.runBlockingTest {
            cartModelImpl.maxQuantity = 10
            cartModelImpl.cartQuantity = 5
            val res = cartModelImpl.createCartProductAndChangeQuantity(FakeData.products.first())
            assertThat(res.quantity,`is`(5))
            assertThat(cartModelImpl.cartQuantity,`is`(0))
            assertThat(cartModelImpl.maxQuantity,`is`(5))

            val addToCartCount = cartModelImpl.addToCartCount.getOrAwaitValue().getContentIfNotHandled()
            assertThat(addToCartCount,`is`(5))
        }
    }

}