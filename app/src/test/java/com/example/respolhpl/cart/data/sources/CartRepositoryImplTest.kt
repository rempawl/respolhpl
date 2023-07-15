package com.example.respolhpl.cart.data.sources

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.sources.repository.CartRepositoryImpl
import com.example.respolhpl.utils.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartRepositoryImplTest : BaseCoroutineTest() {

    lateinit var repository: CartRepositoryImpl

    //    lateinit var dao: CartProductDao
    val dispatchersProvider = TestDispatchersProvider()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
//        dao = FakeCartProductDao()
//        repository = CartRepositoryImpl(dao, dispatchersProvider)
    }

    @Test
    fun getProducts() {
        /*  coroutineTestRule.runBlockingTest {
              val prod = repository.getProducts().first()
              prod.checkIfIsSuccessAndListOf<CartProduct>()?.let { prods ->
                  assertThat(prods, `is`(FakeData.cartProducts))
              }
          }*/
    }

    @Test
    fun addExistingProduct() {
        /*coroutineTestRule.runBlockingTest {
            val res = repository.getProducts().first()
                .checkIfIsSuccessAndListOf<CartProduct>() ?: throw IllegalStateException()

            val prod = res.first()
            val updated = prod.copy(quantity = 4)

            repository.addProduct(updated)

            repository.getProducts().first().checkIfIsSuccessAndListOf<CartProduct>()
                ?.let { prods ->
                    val r = prods.find { it.id == updated.id }
                    val quantity = prod.quantity + updated.quantity
                    val expected = updated.copy(quantity = quantity, cost = quantity * prod.price)
                    assertThat(r, `is`(expected))
                }
        }*/
    }

    @Test
    fun addNewProduct() {
        /*coroutineTestRule.runBlockingTest {
            val prod = CartProduct(id = 1, name = "test", quantity = 5, "src", 2.50)
            repository.addProduct(prod)
            repository.getProducts().first().checkIfIsSuccessAndListOf<CartProduct>()
                ?.let { prods ->
                    val res = prods.find { it == prod }
                    assertThat(res, `is`(prod))
                }
        }*/
    }

    @Test
    fun deleteProduct() {
        /*coroutineTestRule.runBlockingTest {
            val product = FakeData.cartProducts.first()

            repository.delete(product)
            repository.getProducts().first().checkIfIsSuccessAndListOf<CartProduct>()
                ?.let { prods ->
                    assertNull(prods.find { it == product })
                }

        }
*/
    }
}
