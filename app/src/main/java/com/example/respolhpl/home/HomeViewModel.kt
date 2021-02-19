package com.example.respolhpl.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.sources.RemoteDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val remoteDataSource: RemoteDataSource) :
    ViewModel() {

    private val _data = MutableLiveData<Result<*>>()
    val data: LiveData<Result<*>>
        get() = _data


    init {
        dosth()
    }

    private fun dosth() {
        viewModelScope.launch {
            _data.value = Result.Loading
            try {
                val res = remoteDataSource.getAllProductsAsync().await()
                val prod = Product.from(res)
                _data.value = Result.Success(prod)
            } catch (e: Exception) {
                _data.value = Result.Error(e)
            }
        }
    }

}