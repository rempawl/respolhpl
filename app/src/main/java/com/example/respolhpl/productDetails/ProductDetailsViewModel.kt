package com.example.respolhpl.productDetails


import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.model.domain.CartItem
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.usecase.GetProductUseCase
import com.example.respolhpl.productDetails.currentPageState.ViewPagerPageManager
import com.example.respolhpl.utils.BaseViewModel
import com.example.respolhpl.utils.extensions.DefaultError
import com.example.respolhpl.utils.extensions.onError
import com.example.respolhpl.utils.extensions.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
//     productRepository: ProductRepository,
//    private val cartRepository: CartRepository,
    private val getProductUseCase: GetProductUseCase,
    viewPagerPageManager: ViewPagerPageManager
) : BaseViewModel<ProductDetailsViewModel.ProductDetailsState>(ProductDetailsState()),
    ViewPagerPageManager by viewPagerPageManager {
    //todo maxQuantity
    init {
        observeProgress { isLoading ->
            setState { it.copy(isLoading = isLoading) }
        }
    }

    private val _cartQuantity = MutableStateFlow(0)
    val cartQuantity: StateFlow<Int>
        get() = _cartQuantity

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    val isMinusBtnEnabled: Flow<Boolean> = cartQuantity.map { it > 1 }

    val isPlusBtnEnabled: Flow<Boolean> = cartQuantity.map { it < 10 }

    private val _shouldNavigate = MutableSharedFlow<Int>()
    val shouldNavigate: SharedFlow<Int>
        get() = _shouldNavigate


    private val productId
        get() = (savedStateHandle.get<Int>(KEY_PROD_ID)
            ?: throw java.lang.IllegalStateException("productId is null"))

    init {
        getProduct(productId)
    }

    fun retry() {
        getProduct(productId)
    }

    fun navigateToFullScreenImage(): StateFlow<Int> {
        return currentPage
    }


    fun onAddToCartClick() =
        product.flatMapLatest {
            it!!
            flow {
//                cartRepository.addProduct(createCartProduct(it)) todo usecase
                emit(_cartQuantity.value)
                _cartQuantity.update { 0 }
            }
        }


    fun onMinusBtnClick() {
        _cartQuantity.update { it - 1 }
    }

    fun onPlusBtnClick() {
        _cartQuantity.update { it + 1 }
    }


    private fun createCartProduct(product: Product) = CartItem.CartProduct(
        product.id,
        price = product.price,
        quantity = _cartQuantity.value,
        name = product.name,
        thumbnailSrc = product.thumbnailSrc
    )


    private fun getProduct(id: Int) {
        viewModelScope.launch {
            withProgress(progress) {
                getProductUseCase.call(id)
                    .onSuccess { product -> _product.update { product } }
                    .onError { error ->
                        setState { it.copy(error = error) }
                        Log.d("kruci", "error $error")
                    }
            }
        }
    }

    data class ProductDetailsState(
        val isLoading: Boolean = true,
        val error: DefaultError? = null,
    )

    companion object {
        const val KEY_PROD_ID = "productId"

    }
}
