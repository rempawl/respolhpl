package com.example.respolhpl.productDetails


import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.BR
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.productDetails.currentPageState.CurrentPageState
import com.example.respolhpl.utils.ObservableViewModel
import com.example.respolhpl.utils.event.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository,
    currentPageState: CurrentPageState
) : ObservableViewModel(), CurrentPageState by currentPageState {

    private val _shouldNavigate = MutableLiveData<Event<Unit>>()
    val shouldNavigate: LiveData<Event<Unit>>
        get() = _shouldNavigate

    private var maxQuantity = 1

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
        _shouldNavigate.value = Event(Unit)
    }


    private suspend fun getProduct(id: Int) {
        productRepository.getProductById(id).collect { res ->
            setMaxQuantity(res)
            _result.postValue(res)
        }
    }

    private fun setMaxQuantity(res: Result<*>) {
        res.checkIfIsSuccessAndType<Product>()?.let { prod ->
            maxQuantity = prod.quantity
            notifyPropertyChanged(BR.plusBtnEnabled)
        }
    }


}