package com.example.respolhpl.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.respolhpl.FakeCartProductDao
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.cart.data.sources.CartRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

@ExperimentalCoroutinesApi
class CartViewModelTest{
    lateinit var viewModel : CartViewModel

    lateinit var cartRepository: CartRepository
    private val dispatcherProvider = TestDispatchersProvider()
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        cartRepository = CartRepositoryImpl(FakeCartProductDao(),dispatcherProvider)
        viewModel = CartViewModel(cartRepository)
    }

}