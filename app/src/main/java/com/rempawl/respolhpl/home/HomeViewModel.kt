package com.rempawl.respolhpl.home

import androidx.lifecycle.viewModelScope
import com.rempawl.respolhpl.data.model.domain.ProductMinimal
import com.rempawl.respolhpl.data.usecase.GetProductsUseCase
import com.rempawl.respolhpl.list.paging.PagingConfig
import com.rempawl.respolhpl.list.paging.PagingData
import com.rempawl.respolhpl.list.paging.PagingManager
import com.rempawl.respolhpl.utils.BaseViewModel
import com.rempawl.respolhpl.utils.Effect
import com.rempawl.respolhpl.utils.HtmlParser
import com.rempawl.respolhpl.utils.ProgressSemaphore
import com.rempawl.respolhpl.utils.extensions.mapSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    private val htmlParser: HtmlParser,
    pagingConfig: PagingConfig,
    progressSemaphore: ProgressSemaphore
) : BaseViewModel<HomeState, HomeEffect>(HomeState(), progressSemaphore) {

    private val loadMoreTrigger = MutableSharedFlow<Unit>()

    private val pagingManager = PagingManager(
        config = pagingConfig,
        scope = viewModelScope,
        loadMoreTrigger = loadMoreTrigger,
        idProducer = { it.itemId },
        dataSource = { pagingParam ->
            flow { emit(getProductsUseCase.fresh(pagingParam)) }
                .mapSuccess { it.toListItems() }
        }
    )

    init {
        getProducts()
    }

    fun navigateToProductDetails(id: Int) {
        viewModelScope.launch {
            setEffect(
                HomeEffect.NavigateToProductDetails(
                    DestinationId(id)
                )
            )
        }
    }

    private fun getProducts() {
        viewModelScope.launch {
            pagingManager.pagingData.collectLatest { pagingData ->
                setState {
                    copy(pagingData = pagingData)
                }
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

    private fun List<ProductMinimal>.toListItems() =
        map {
            ProductMinimalListItem(
                productId = it.id,
                name = it.name,
                thumbnailSrc = it.thumbnailSrc,
                priceFormatted = htmlParser.parse(it.priceHtml)
            )
        }
}

data class HomeState(val pagingData: PagingData<ProductMinimalListItem> = PagingData())

sealed interface HomeEffect : Effect {
    data class NavigateToProductDetails(val id: DestinationId) : HomeEffect
}

@JvmInline
value class DestinationId(val id: Int)
