package com.example.respolhpl.cart.data.sources

import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.FakeCartProductDao
import com.example.respolhpl.FakeData
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.cart.data.CartProduct
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartRepositoryImplTest {

    lateinit var repository: CartRepositoryImpl
    lateinit var dao: CartProductDao
    val dispatchersProvider = TestDispatchersProvider()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(dispatcher = dispatchersProvider.test)

    @Before
    fun setup() {
        dao = FakeCartProductDao()
        repository = CartRepositoryImpl(dao, dispatchersProvider)
    }

    @Test
    fun getProducts() {
        coroutineTestRule.runBlockingTest {
            val prod = repository.getProducts().first()
            prod.checkIfIsSuccessAndListOf<CartProduct>()?.let { prods ->
                assertThat(prods, `is`(FakeData.cartProducts))
            }

        }
    }

    @Test
    fun addExistingProduct() {
        coroutineTestRule.runBlockingTest {
            val res = repository.getProducts().first()
                .checkIfIsSuccessAndListOf<CartProduct>() ?: throw IllegalStateException()

            val prod = res.first()
            val updated = prod.copy(quantity = 4)

            repository.addProduct(updated)

            repository.getProducts().first().checkIfIsSuccessAndListOf<CartProduct>()
                ?.let { prods ->
                    val r = prods.find { it.id == updated.id }
                    val expected = updated.copy(quantity = prod.quantity + updated.quantity)
                    assertThat(r, `is`(expected))
                }
        }
    }

    @Test
    fun addNewProduct() {
        coroutineTestRule.runBlockingTest {

        }

    }
}
