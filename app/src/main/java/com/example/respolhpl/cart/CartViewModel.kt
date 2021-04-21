package com.example.respolhpl.cart

import androidx.lifecycle.ViewModel
import com.example.respolhpl.cart.data.sources.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(cartRepository: CartRepository) : ViewModel(){

}
