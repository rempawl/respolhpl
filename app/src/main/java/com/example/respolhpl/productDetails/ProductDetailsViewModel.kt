package com.example.respolhpl.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.Repository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class ProductDetailsViewModel @AssistedInject constructor(
    @Assisted id: Long, private val repository: Repository,
    val orderModel: OrderModel
) : ViewModel() {

    @AssistedFactory
    interface ProductDetailsViewModelFactory {
        fun create(id: Long): ProductDetailsViewModel
    }

    private val _result = MutableLiveData<Result<*>>(Result.Loading)
    val result: LiveData<Result<*>>
        get() = _result


    init {
        viewModelScope.launch {
            getProduct(id)
//            Log.d("kruci",)
        }

    }

    private suspend fun getProduct(id: Long) {
        _result.value = repository.getProductById(id)

    }

    companion object
}