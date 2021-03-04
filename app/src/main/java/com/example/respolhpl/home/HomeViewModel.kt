package com.example.respolhpl.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.RemoteDataSource
import com.example.respolhpl.data.sources.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: Repository) :
    ViewModel() {

    private val _data = MutableLiveData<Result<*>>(Result.Loading)
    val data: LiveData<Result<*>>
        get() = _data


    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            _data.value = repository.getProducts()
        }
    }


}