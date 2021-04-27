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


//todo change quantity while adding to cart
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    private val cartRepository: CartRepository,
    currentViewPagerPage: CurrentViewPagerPage
) : ObservableViewModel(), CurrentViewPagerPage by currentViewPagerPage {

    private val _shouldNavigate = MediatorLiveData<Event<Int>>()
    val shouldNavigate: LiveData<Event<Int>>
        get() = _shouldNavigate

    private val currentPageEvent = currentPage.map { Event(it) }

    private var maxQuantity = 0

    val isMinusBtnEnabled: Boolean
        @Bindable
        get() = orderQuantity > 1

    val isPlusBtnEnabled: Boolean
        @Bindable
        get() = orderQuantity < maxQuantity

    @Bindable
    var orderQuantity: Int = 1
        set(value) {
            field = if (value > maxQuantity) maxQuantity else value
            notifyPropertyChanged(BR.orderQuantity)
            notifyPropertyChanged(BR.minusBtnEnabled)
            notifyPropertyChanged(BR.plusBtnEnabled)
        }

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result

    private val prodObserver = Observer<Result<*>> { it ->
        it.checkIfIsSuccessAndType<Product>()?.let { createCartProductAndAddToCart(it) }
    }

    init {
        viewModelScope.launch {
            getProduct(savedStateHandle.get<Int>(ProductDetailsFragment.prodId) ?: -1)
        }
    }


    fun onMinusBtnClick() {
        orderQuantity -= 1
    }

    fun onPlusBtnClick() {
        orderQuantity += 1
    }

    fun navigate() {
        _shouldNavigate.addSource(currentPageEvent) { _shouldNavigate.value = it }
    }

    fun doneNavigating() {
        _shouldNavigate.removeSource(currentPageEvent)
    }

    fun onAddToCartClick() {

        //todo rethink
        _result.observeForever(prodObserver)
    }

    private fun createCartProduct(product: Product) = CartProduct(
        product.id,
        price = product.price,
        quantity = orderQuantity,
        name = product.name,
        thumbnailSrc = product.thumbnailSrc
    )

    private fun createCartProductAndAddToCart(product: Product) {
        viewModelScope.launch {
            cartRepository.addProduct(createCartProduct(product))
            _result.removeObserver(prodObserver)
        }
    }

    private suspend fun getProduct(id: Int) {
        productRepository.getProductById(id).collectLatest { res ->
            _result.postValue(res)
            setMaxQuantity(res)
        }

    }

    private fun setMaxQuantity(res: Result<*>) {
        res.checkIfIsSuccessAndType<Product>()?.let { prod ->
            maxQuantity = prod.quantity
            notifyPropertyChanged(BR.plusBtnEnabled)
        }
    }

}