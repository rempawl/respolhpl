package com.example.respolhpl.productDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.sources.RemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class ProductDetailsViewModel @AssistedInject constructor(
    @Assisted id: Long, private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    @AssistedFactory
    interface ProductDetailsViewModelFactory {
        fun create(id: Long): ProductDetailsViewModel
    }

    private val _product = MutableLiveData<Result<*>>()
    val product: LiveData<Result<*>>
        get() = _product

    init {
        viewModelScope.launch { getProduct(id) }
    }

    suspend fun getProduct(id: Long) {
        try {
            val res = remoteDataSource.getProductByIdAsync(id).await()
            _product.value = Result.Success(Product.from(res))
        } catch (e: Exception) {
            _product.value = Result.Error(e)
        }
    }

    companion object
}