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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle,
    private val dispatchersProvider: DispatchersProvider
) : ObservableViewModel() {

    suspend fun getProducts(): Flow<PagingData<ProductMinimal>> {
        return productRepository.getProducts()
            .cachedIn(viewModelScope)
    }
}