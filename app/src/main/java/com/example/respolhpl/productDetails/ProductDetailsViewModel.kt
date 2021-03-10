package com.example.respolhpl.productDetails


import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.BR
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.sources.ProductRepository
import com.example.respolhpl.utils.DispatchersProvider
import com.example.respolhpl.utils.ObservableViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val productRepository: ProductRepository
) : ObservableViewModel() {

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


    fun onMinusBtnClick() {
        orderQuantity -= 1
    }

    fun onPlusBtnClick() {
        orderQuantity += 1
    }


    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result

    init {
        viewModelScope.launch {
            getProduct(savedStateHandle.get<Long>("productId") ?: -1)
        }
    }

    private suspend fun getProduct(id: Long) {
        val res = productRepository.getProductById(id)
        setMaxQuantity(res)
        _result.value = res
    }

    private fun setMaxQuantity(res: Result<*>) {
        res.takeIf { it.isSuccess }?.let {
            res as Result.Success
            check(res.data is Product) { "data should be product" }
            maxQuantity = res.data.quantity
            notifyPropertyChanged(BR.plusBtnEnabled)
        }
    }


}