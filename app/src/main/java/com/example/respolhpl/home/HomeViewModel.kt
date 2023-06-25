package com.example.respolhpl.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
) : BaseViewModel<Unit>(Unit) {

    private val _shouldNavigate = MutableSharedFlow<Int>()
    val shouldNavigate: SharedFlow<Int>
        get() = _shouldNavigate.asSharedFlow()


    private val _items = MutableStateFlow<PagingData<ProductMinimal>>(PagingData.empty())
    val items: StateFlow<PagingData<ProductMinimal>> = _items.asStateFlow()

    init {
        getProducts()
    }

    fun navigate(id: Int) {
        viewModelScope.launch { _shouldNavigate.emit(id) }
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts()
                .cachedIn(viewModelScope)
                .collectLatest { items -> _items.update { items } }
        }

    }
}

