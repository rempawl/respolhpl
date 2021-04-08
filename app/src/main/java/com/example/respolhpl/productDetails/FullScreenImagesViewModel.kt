package com.example.respolhpl.productDetails

import androidx.lifecycle.*
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FullScreenImagesViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result

    init {
        viewModelScope.launch { getImages() }
    }

    private suspend fun getImages() {
        val id = savedStateHandle.get<Int>(ProductDetailsFragment.prodId) ?: -1
        productRepository.getProductImages(id).collect {
            _result.postValue(it)
        }
    }

}