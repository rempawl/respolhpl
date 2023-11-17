package com.example.respolhpl.home

import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.usecase.GetProductsUseCase
import com.example.respolhpl.paging.BaseListItem
import com.example.respolhpl.paging.PagingConfig
import com.example.respolhpl.paging.PagingData
import com.example.respolhpl.paging.PagingManager
import com.example.respolhpl.utils.BaseViewModel
import com.example.respolhpl.utils.NoEffects
import com.example.respolhpl.utils.extensions.mapSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val pagingConfig: PagingConfig
) : BaseViewModel<Unit, NoEffects>(Unit) {

    private val _shouldNavigate = MutableSharedFlow<DestinationId>()
    val shouldNavigate: SharedFlow<DestinationId>
        get() = _shouldNavigate.asSharedFlow()

    private val loadMoreTrigger = MutableSharedFlow<Unit>()
    private val favoriteItems = MutableStateFlow<List<Int>>(emptyList())

    private val pagingManager = PagingManager(
        config = pagingConfig,
        scope = viewModelScope,
        loadMoreTrigger = loadMoreTrigger,
        idProducer = { it.itemId },
        dataSource = { pagingParam ->
            getProductsUseCase.cacheAndFresh(pagingParam).mapSuccess { it.toListItems() }
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
            combine(pagingManager.pagingData, favoriteItems) { pagingData, favorites ->
                pagingData.copy(items = pagingData.items.map { item ->
                    item.copy(isLiked = favorites.contains(item.product.id))
                })
            }
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
        val isLiked: Boolean = false,
        override val itemId: Int = product.id
    ) : BaseListItem
}


@JvmInline
value class DestinationId(val id: Int)
