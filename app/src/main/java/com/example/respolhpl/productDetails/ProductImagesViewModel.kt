package com.example.respolhpl.productDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProductImagesViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
    viewPagerPageManager: ViewPagerPageManager
) : ViewModel(), ViewPagerPageManager by viewPagerPageManager {

    private val _result = MutableStateFlow<Result<*>>(Result.Loading)
    val result: StateFlow<Result<*>>
        get() = _result
    private val id = savedStateHandle.get<Int>(ProductDetailsViewModel.KEY_PROD_ID)
        ?: throw IllegalStateException(" product id is null")

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
//            productRepository
//                .getProductImages(id)
//                .onEach { _result.value = Result.Loading }
//                .collect {
//                    _result.update { it }
//                    saveCurrentPage(savedStateHandle.get<Int>("currentPage") ?: 0)
//                }
        }
    }

    fun retry() {
        //todo trigger some flow
        getImages()
    }


}
