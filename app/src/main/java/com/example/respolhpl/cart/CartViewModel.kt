package com.example.respolhpl.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result

    private val _isEmpty = MutableLiveData<Boolean>()
    val isEmpty: LiveData<Boolean>
        get() = _isEmpty

    private val _cartCost = MutableLiveData<Double>()
    val cartCost: LiveData<Double>
        get() = _cartCost

    init {
        viewModelScope.launch {
            getCart()
        }
    }


    fun deleteFromCart(product: CartProduct) {
        viewModelScope.launch {
            cartRepository.delete(product)
        }
    }

    private suspend fun getCart() {
        cartRepository.getProducts().onEach {
            updateCartState(it)
        }.collectLatest {
            _result.postValue(it)
        }
    }

    private fun updateCartState(result: Result<*>) {
        updateCartCostIfSuccess(result)
        val isEmpty = (result.checkIfIsSuccessAndListOf<CartProduct>()?.size ?: 0) == 0
        _isEmpty.postValue(isEmpty)
    }

    private fun updateCartCostIfSuccess(result: Result<*>) {
        result.checkIfIsSuccessAndListOf<CartProduct>()
            ?.sumOf { it.cost }
            ?.let { _cartCost.postValue(it) }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

}
