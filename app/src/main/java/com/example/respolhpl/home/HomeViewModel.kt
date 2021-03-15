package com.example.respolhpl.home

import androidx.lifecycle.*
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.ProductRepository
import com.example.respolhpl.utils.ResultViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
) :ResultViewModel() {

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    override val result: LiveData<Result<*>>
        get() = _result


    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _result.value = productRepository.getProducts()

        }
    }


}