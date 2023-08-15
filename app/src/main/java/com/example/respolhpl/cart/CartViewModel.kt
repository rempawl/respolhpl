package com.example.respolhpl.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.CartItem
import com.example.respolhpl.data.sources.repository.CartRepository
import com.example.respolhpl.utils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor() : ViewModel() {

    fun deleteFromCart(product: CartItem.CartProduct) {
        viewModelScope.launch {
        }
    }

    fun clearCart() {
        viewModelScope.launch {
        }
    }

}
