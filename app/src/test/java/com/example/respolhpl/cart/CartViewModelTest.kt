package com.example.respolhpl.cart

import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.sources.repository.CartRepository
import com.example.respolhpl.data.sources.repository.CartRepositoryImpl
import com.example.respolhpl.utils.BaseCoroutineTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest : BaseCoroutineTest() {
    lateinit var viewModel: CartViewModel
    lateinit var cartRepository: CartRepository
    private val dispatcherProvider = TestDispatchersProvider()


    @Before
    fun setup() {
        cartRepository = CartRepositoryImpl(dispatcherProvider)
        viewModel = CartViewModel(cartRepository)
    }

    @Test
    fun init() = runTest {
//        val res = viewModel.result.firstOrNull()
//        assertNotNull(res)
//            res?.checkIfIsSuccessAndListOf<CartItem.CartProduct>()?.let { prods ->
//                assertThat(prods, `is`(FakeData.cartProducts))
//            }
    }


    @Test
    fun delete() = runTest {
//        val prod = FakeData.cartProducts.firstOrNull()
//        assertNotNull(prod)
//            viewModel.deleteFromCart(prod!!)
//            viewModel.result.first().checkIfIsSuccessAndListOf<CartProduct>()
//                ?.let { prods ->
//                    assertNull(prods.find { prod == it })
//                }
    }
}
