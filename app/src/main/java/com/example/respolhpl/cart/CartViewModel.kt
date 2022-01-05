package com.example.respolhpl.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {


    private val _result = MutableStateFlow<Result<*>>(Result.Loading)
    val result: StateFlow<Result<*>>
        get() = _result
    private val successRes = result.filter { it.isSuccess }

    val isEmpty: Flow<Boolean> = successRes.map {
        (it as Result.Success<List<*>>).data.isEmpty()
    }

    val cartCost: Flow<Double> = successRes.map {
        (it as Result.Success<List<CartProduct>>).data.sumOf { it.cost }
    }

    init {
        viewModelScope.launch {
            cartRepository.getProducts().collectLatest {
                _result.value = it
            }
        }
    }

    fun deleteFromCart(product: CartProduct) {
        viewModelScope.launch {
            cartRepository.delete(product)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

}
