package com.example.respolhpl.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.utils.ObservableViewModel
import com.example.respolhpl.utils.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val savedStateHandle: SavedStateHandle
) : ObservableViewModel() {

    private val _shouldNavigate = MutableLiveData<Event<Int>>()
    val shouldNavigate: LiveData<Event<Int>>
        get() = _shouldNavigate

    private var _result: Flow<PagingData<ProductMinimal>>? = null
    val result: Flow<PagingData<ProductMinimal>>?
        get() = _result

    init {
        viewModelScope.launch {
            getProducts()
        }
    }

    fun navigate(id: Int) {
        _shouldNavigate.value = Event(id)
    }

    private suspend fun getProducts() {
        _result = productRepository.getProducts()
            .cachedIn(viewModelScope)
    }
}

