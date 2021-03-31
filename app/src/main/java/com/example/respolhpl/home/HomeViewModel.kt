package com.example.respolhpl.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ObservableViewModel() {

    var result: Flow<PagingData<ProductMinimal>>? = null

    init {
        viewModelScope.launch {
            getProducts()
        }
    }

    private suspend fun getProducts() {
        result = productRepository.getProducts()
            .cachedIn(viewModelScope)
    }
}