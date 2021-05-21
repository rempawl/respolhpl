package com.example.respolhpl.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import javax.inject.Inject

class NetworkListenerImpl @Inject constructor() : NetworkListener {
    private val _isConnected = MutableLiveData<Boolean>(true)
    override val isConnected: LiveData<Boolean>
        get() = _isConnected

    override fun onAvailable() {
        _isConnected.postValue(true)
    }

    override fun onUnavailable() {
        _isConnected.postValue(false)
    }
}