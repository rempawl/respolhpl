package com.example.respolhpl.productDetails


import android.util.Log
import androidx.lifecycle.*
import com.example.respolhpl.cart.data.CartItem
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.productDetails.currentPageState.CurrentViewPagerPage
import com.example.respolhpl.utils.ObservableViewModel
import com.example.respolhpl.utils.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

//todo change quantity while adding to cart and set timeout for block
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    currentViewPagerPage: CurrentViewPagerPage
) : ObservableViewModel(), CurrentViewPagerPage by currentViewPagerPage {
    //todo maxQuantity

    private val _cartQuantity = MutableStateFlow(0)
    val cartQuantity: StateFlow<Int>
        get() = _cartQuantity

    val isMinusBtnEnabled: Flow<Boolean> = cartQuantity.map {
        it > 1
    }

    val isPlusBtnEnabled = cartQuantity.map {
        it < 10
    }

    private val _shouldNavigate = MediatorLiveData<Event<Int>>()
    val shouldNavigate: LiveData<Event<Int>>
        get() = _shouldNavigate

    private val _result = MutableStateFlow<Result<*>>(Result.Loading)
    val result: StateFlow<Result<*>>
        get() = _result

    private val productId = getProdId()

    init {
        getProduct(productId)
    }

    fun retry() {
        getProduct(productId)
    }

    fun navigateToFullScreenImage() : StateFlow<Int> {
        return currentPage
    }


    fun onAddToCartClick(): Flow<Int> {
        return _result.value.checkIfIsSuccessAndType<Product>()?.let {
            flow {
                cartRepository.addProduct(createCartProduct(it))
                emit(_cartQuantity.value)
                _cartQuantity.update { 0 }
                //todo set maxQuant
            }
        } ?: emptyFlow<Int>()
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
            productRepository.getProductById(id).collectLatest { res ->
                _result.value = (res)
                setMaxQuantityAndProduct(res)
            }
        }
    }

    private fun setMaxQuantityAndProduct(res: Result<*>) {
        res.checkIfIsSuccessAndType<Product>()?.let { prod ->
//            maxQuantity = (prod.quantity)
        }

    }

    private fun getProdId() = (savedStateHandle.get<Int>(ProductDetailsFragment.prodId)
        ?: throw java.lang.IllegalStateException("productId is null"))


}