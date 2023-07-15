package com.example.respolhpl.home

import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.paging.PagingData
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository // todo usecase
) : BaseViewModel<Unit>(Unit) {

    private val _shouldNavigate = MutableSharedFlow<DestinationId>()
    val shouldNavigate: SharedFlow<DestinationId>
        get() = _shouldNavigate.asSharedFlow()


    private val _items = MutableStateFlow<PagingData<ProductMinimal>>(PagingData())
    val items: StateFlow<PagingData<ProductMinimal>> = _items.asStateFlow()

    init {
        getProducts()
    }

    fun navigate(id: Int) {
        viewModelScope.launch { _shouldNavigate.emit(DestinationId(id)) }
    }

    private fun getProducts() {
        viewModelScope.launch {
            productRepository.getProducts()
//                .cachedIn(viewModelScope)
                .collectLatest { items -> _items.update { items } }
        }
    }


}

@JvmInline
value class DestinationId(val id: Int)
