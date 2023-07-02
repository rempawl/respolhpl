@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.respolhpl.productDetails


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.CartItem
import com.example.respolhpl.data.model.domain.Images
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.usecase.GetProductUseCase
import com.example.respolhpl.utils.BaseViewModel
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.onError
import com.example.respolhpl.utils.extensions.onSuccess
import com.example.respolhpl.utils.watchProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
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
//    private val cartRepository: CartRepository, add to cart usecase
    private val getProductUseCase: GetProductUseCase,
) : BaseViewModel<ProductDetailsViewModel.ProductDetailsState>(ProductDetailsState()) {

    init {
        observeProgress { isLoading ->
            setState { copy(isLoading = isLoading) }
        }
    }

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

//    private val _shouldNavigate = MutableSharedFlow<Int>()
//    val shouldNavigate: SharedFlow<Int>
//        get() = _shouldNavigate


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

    fun onAddToCartClick() = Unit
//        product.flatMapLatest {
//            it
//            flow {
////                cartRepository.addProduct(createCartProduct(it)) todo usecase
//                emit(_cartQuantity.value)
//                _cartQuantity.update { 0 }
//            }
//        }


    fun onMinusBtnClick() {
        _cartQuantity.update { it - 1 }
    }

    fun onPlusBtnClick() {
        _cartQuantity.update { it + 1 }
    }

    fun setQuantityChangedListener(quantityText: Flow<CharSequence>) =
        quantityText
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


    private fun createCartProduct(product: Product) = CartItem.CartProduct(
        product.id,
        price = product.price,
        quantity = _cartQuantity.value,
        name = product.name,
        thumbnailSrc = product.thumbnailSrc
    )

    private fun getProduct(id: Int) {
        getProductUseCase.cacheAndFresh(id)
            .watchProgress(progress)
            .onSuccess { product ->
                setState { copy(error = null) }
                _product.update { product }
            }
            .onError { error ->
                setState { copy(error = error) }
            }
            .launchIn(viewModelScope)
    }

    data class ProductDetailsState(
        val isLoading: Boolean = true,
        val error: DefaultError? = null,
    ) {
        val isSuccess: Boolean
            get() = !isLoading && error == null
    }

    companion object {
        const val KEY_PROD_ID = "productId"

    }
}
