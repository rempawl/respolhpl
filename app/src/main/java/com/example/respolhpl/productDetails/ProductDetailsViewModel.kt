package com.example.respolhpl.productDetails


import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.example.respolhpl.BR
import com.example.respolhpl.cart.data.CartProduct
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
    currentViewPagerPage: CurrentViewPagerPage
) : ObservableViewModel(), CurrentViewPagerPage by currentViewPagerPage {

    val isMinusBtnEnabled: Boolean
        @Bindable
        get() = cartQuantity > 1

    val isPlusBtnEnabled: Boolean
        @Bindable
        get() = cartQuantity < maxQuantity

    @Bindable
    var cartQuantity: Int = 0
        set(value) {
            field = if (value > maxQuantity) maxQuantity else value
            notifyPropertyChanged(BR.cartQuantity)
            notifyPropertyChanged(BR.minusBtnEnabled)
            notifyPropertyChanged(BR.plusBtnEnabled)
        }

    @Bindable
    var maxQuantity = 0
        private set(value) {
            field = value
            notifyPropertyChanged(BR.plusBtnEnabled)
            notifyPropertyChanged(BR.maxQuantity)
        }

    private val _shouldNavigate = MediatorLiveData<Event<Int>>()
    val shouldNavigate: LiveData<Event<Int>>
        get() = _shouldNavigate

    private val _addToCartCount = MutableLiveData<Event<Int>>()
    val addToCartCount: LiveData<Event<Int>>
        get() = _addToCartCount

    private val currentPageEvent = currentPage.map { Event(it) }

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result

    private var product: Product? = null

    init {
        viewModelScope.launch {
            getProduct(savedStateHandle.get<Int>(ProductDetailsFragment.prodId) ?: -1)
        }
    }


    fun onMinusBtnClick() {
        cartQuantity -= 1
    }

    fun onPlusBtnClick() {
        cartQuantity += 1
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

    private fun createCartProduct(product: Product) = CartProduct(
        product.id,
        price = product.price,
        quantity = cartQuantity,
        name = product.name,
        thumbnailSrc = product.thumbnailSrc
    )

    private fun createCartProductAndAddToCart(product: Product) {
        viewModelScope.launch {
            _addToCartCount.value = Event(cartQuantity)
            cartRepository.addProduct(createCartProduct(product))
            maxQuantity -= cartQuantity
            cartQuantity = 0

        }
    }

    private suspend fun getProduct(id: Int) {
        productRepository.getProductById(id).collectLatest { res ->
            _result.postValue(res)
            setMaxQuantityAndCreateCartProduct(res)
        }
    }

    private fun setMaxQuantityAndCreateCartProduct(res: Result<*>) {
        res.checkIfIsSuccessAndType<Product>()?.let { prod ->
            maxQuantity = (prod.quantity)
            product = prod
        }

    }


}