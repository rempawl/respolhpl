package com.example.respolhpl.home

import androidx.lifecycle.*
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.sources.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val savedStateHandle: SavedStateHandle
) :
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