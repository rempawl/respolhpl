package com.example.respolhpl.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(private val cartRepository: CartRepository) : ViewModel() {

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result

    init {
        viewModelScope.launch {
            getCart()
        }
    }

    private suspend fun getCart() {
        cartRepository.getProducts().collectLatest {
            _result.postValue(it)
        }
    }
}
