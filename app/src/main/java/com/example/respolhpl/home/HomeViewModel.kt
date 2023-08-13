package com.example.respolhpl.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.usecase.GetProductsUseCase
import com.example.respolhpl.paging.BaseListItem
import com.example.respolhpl.paging.PagingConfig
import com.example.respolhpl.paging.PagingData
import com.example.respolhpl.paging.PagingManager
import com.example.respolhpl.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val pagingConfig: PagingConfig
) : BaseViewModel<Unit>(Unit) {

    private val _shouldNavigate = MutableSharedFlow<DestinationId>()
    val shouldNavigate: SharedFlow<DestinationId>
        get() = _shouldNavigate.asSharedFlow()

    private val loadMoreTrigger = MutableSharedFlow<Unit>()

    private val pagingManager = PagingManager<ProductMinimalListItem>(
        config = pagingConfig,
        scope = viewModelScope,
        loadMoreTrigger = loadMoreTrigger,
        idProducer = { it.itemId },
        dataSource = { pagingParam ->
            flow {
                emit(
                    getProductsUseCase.fresh(pagingParam).map { it.toListItems() }
                )
            }
        }
    )

    private val _pagingData = MutableStateFlow<PagingData<ProductMinimalListItem>>(PagingData())
    val pagingData: StateFlow<PagingData<ProductMinimalListItem>> = _pagingData.asStateFlow()

    init {
        getProducts()
    }

    fun navigateToProductDetails(id: Int) {
        viewModelScope.launch { _shouldNavigate.emit(DestinationId(id)) }
    }

    private fun getProducts() {
        viewModelScope.launch {
            pagingManager.pagingData
                .collectLatest { pagingData ->
                    _pagingData.update { pagingData }
                }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            loadMoreTrigger.emit(Unit)
        }
    }

    fun retry() {
        viewModelScope.launch {
            pagingManager.retry()
        }
    }

    private fun List<ProductMinimal>.toListItems() = map { ProductMinimalListItem(it) }

    data class ProductMinimalListItem(
        val product: ProductMinimal,
        override val itemId: Int = product.id
    ) : BaseListItem
}


@JvmInline
value class DestinationId(val id: Int)
