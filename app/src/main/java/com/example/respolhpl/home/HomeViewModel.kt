package com.example.respolhpl.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.sources.RemoteDataSource
import com.example.respolhpl.data.sources.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val remoteDataSource: RemoteDataSource) : ViewModel() {
    val data = MutableLiveData<Result<*>>()

    init {
        dosth()
    }

    private fun dosth() {
        viewModelScope.launch {
            try {


            } catch (e: Exception) {
//                data.value = e.message
            }
        }
    }

}