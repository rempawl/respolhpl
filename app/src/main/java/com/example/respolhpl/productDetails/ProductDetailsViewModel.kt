package com.example.respolhpl.productDetails


import androidx.lifecycle.*
import com.example.respolhpl.cart.data.sources.CartRepository
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.productDetails.currentPageState.CurrentViewPagerPage
import com.example.respolhpl.utils.ObservableViewModel
import com.example.respolhpl.utils.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

//todo change quantity while adding to cart and set timeout for block
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    val cartModel: CartModel,
    currentViewPagerPage: CurrentViewPagerPage
) : ObservableViewModel(), CurrentViewPagerPage by currentViewPagerPage {

    private val _shouldNavigate = MediatorLiveData<Event<Int>>()
    val shouldNavigate: LiveData<Event<Int>>
        get() = _shouldNavigate
    private val currentPageEvent = currentPage.map { Event(it) }

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result

    private var product: Product? = null
    private val productId = getProdId()


    init {
        getProduct(productId)
    }

    fun retry() {
        getProduct(productId)
    }

    fun navigate() {
        _shouldNavigate.addSource(currentPageEvent) { _shouldNavigate.value = it }
    }

    fun doneNavigating() {
        _shouldNavigate.removeSource(currentPageEvent)
    }

    fun onAddToCartClick() {
        product?.let { createCartProductAndAddToCart(it) }
            ?: throw IllegalStateException("cartProduct is not initalized")
    }

    private fun createCartProductAndAddToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addProduct(cartModel.createCartProductAndChangeQuantity(product))
        }
    }

    private fun getProduct(id: Int) {
        viewModelScope.launch {
            _result.value = Result.Loading
            productRepository.getProductById(id).collectLatest { res ->
                _result.postValue(res)
                setMaxQuantityAndProduct(res)
            }
        }
    }

    private fun setMaxQuantityAndProduct(res: Result<*>) {
        res.checkIfIsSuccessAndType<Product>()?.let { prod ->
            cartModel.maxQuantity = (prod.quantity)
            product = prod
        }

    }

    private fun getProdId() = (savedStateHandle.get<Int>(ProductDetailsFragment.prodId)
        ?: throw java.lang.IllegalStateException("productId is null"))


}