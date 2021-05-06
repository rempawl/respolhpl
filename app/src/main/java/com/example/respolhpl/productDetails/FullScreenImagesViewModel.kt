package com.example.respolhpl.productDetails

import androidx.lifecycle.*
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.productDetails.currentPageState.CurrentViewPagerPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FullScreenImagesViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
    currentViewPagerPage: CurrentViewPagerPage
) : ViewModel(), CurrentViewPagerPage by currentViewPagerPage {

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result
    private val id = savedStateHandle.get<Int>(ProductDetailsFragment.prodId) ?: throw
    IllegalStateException(" product id is null")

    init {
        getImages()
    }

    private fun getImages() {
        viewModelScope.launch {
            _result.value = Result.Loading
            productRepository.getProductImages(id).collect {
                _result.postValue(it)
                saveCurrentPage(savedStateHandle.get<Int>("currentPage") ?: 0)
            }
        }
    }

    fun retry() {
        getImages()
    }


}