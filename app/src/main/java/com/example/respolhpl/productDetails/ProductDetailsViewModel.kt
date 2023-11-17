@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.respolhpl.productDetails


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.Images
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.usecase.AddToCartUseCase
import com.example.respolhpl.data.usecase.AddToCartUseCase.Param
import com.example.respolhpl.data.usecase.GetProductUseCase
import com.example.respolhpl.productDetails.ProductDetailsViewModel.*
import com.example.respolhpl.utils.BaseViewModel
import com.example.respolhpl.utils.DefaultError
import com.example.respolhpl.utils.NoEffects
import com.example.respolhpl.utils.extensions.onError
import com.example.respolhpl.utils.extensions.onSuccess
import com.example.respolhpl.utils.watchProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.math.min

// todo pass current image position to full screen image
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getProductUseCase: GetProductUseCase,
    private val addToCartUseCase: AddToCartUseCase
) : BaseViewModel<ProductDetailsState, NoEffects>(ProductDetailsState()) {

    init {
        observeProgress { isLoading ->
            setState { copy(isLoading = isLoading) }
        }
    }

    private val _itemAddedToCart = MutableSharedFlow<Int>()
    val itemAddedToCart = _itemAddedToCart.asSharedFlow()

    private val _cartQuantity = MutableStateFlow(1)
    val cartQuantity: StateFlow<Int>
        get() = _cartQuantity.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow().filterNotNull()

    private val maxQuantity = product.filterNotNull().map { it.quantity }
    val isMinusBtnEnabled: Flow<Boolean> = cartQuantity.map { it > 1 }
    val isPlusBtnEnabled: Flow<Boolean> =
        maxQuantity.combine(cartQuantity) { maxQuantity, currentQuantity ->
            currentQuantity < maxQuantity
        }


    private val productId
        get() = (savedStateHandle.get<Int>(KEY_PROD_ID)
            ?: throw java.lang.IllegalStateException("productId is null"))

    init {
        getProduct(productId)
    }

    fun retry() {
        getProduct(productId)
    }

    fun navigateToFullScreenImage() = product.map { Images(it.images) }.take(1)

    fun onAddToCartClick() {
        combine(product, cartQuantity) { product, quantity ->
            product.id to quantity
        }
            .flatMapLatest { (id, quantity) ->
                addToCartUseCase.call(Param(id, quantity))
            }
            .take(1)
            .onSuccess {
                _itemAddedToCart.emit(_cartQuantity.value)
                _cartQuantity.update { 1 }
            }
            .onError { error ->
                addError(error)
            }
            .launchIn(viewModelScope)
    }


    fun onMinusBtnClick() {
        _cartQuantity.update { it - 1 }
    }

    fun onPlusBtnClick() {
        _cartQuantity.update { it + 1 }
    }

    fun setQuantityChangedListener(quantityText: Flow<CharSequence>) =
        quantityText
            .distinctUntilChanged()
            .mapLatest { it.toString() }
            .filter { it.isNotBlank() }
            .map { it.toInt() }
            .combine(maxQuantity) { quantity, maxQuantity ->
                min(quantity, maxQuantity)
            }
            .onEach { quantity ->
                _cartQuantity.update { quantity }
            }
            .launchIn(viewModelScope)


    private fun getProduct(id: Int) {
        getProductUseCase.cacheAndFresh(id)
            .watchProgress(progress)
            .onSuccess { product ->
                setState { copy(productError = null) }
                _product.update { product }
            }
            .onError { error ->
                setState { copy(productError = error) }
            }
            .launchIn(viewModelScope)
    }

    data class ProductDetailsState(
        val isLoading: Boolean = true,
        val productError: DefaultError? = null,
    ) {
        val isSuccess: Boolean
            get() = !isLoading && productError == null
    }

    companion object {
        const val KEY_PROD_ID = "productId"
    }
}
