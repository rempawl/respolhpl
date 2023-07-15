package com.example.respolhpl.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.CartItem
import com.example.respolhpl.data.sources.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {


//    private val _result = MutableStateFlow<Result<*>>(Result.Loading)
//    val result: StateFlow<Result<*>>
//        get() = _result
//    private val successRes = result.filter { it.isSuccess }
//
//    val isEmpty: Flow<Boolean> = successRes.map {
//        (it as Result.Success<List<*>>).data.isEmpty()
//    }

    init {
//        viewModelScope.launch {
//            cartRepository.getProducts().collectLatest {
//                _result.value = it
//            }
//        }
//        _result.update { it }
    }

    fun deleteFromCart(product: CartItem.CartProduct) {
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
