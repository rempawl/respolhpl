package com.example.respolhpl.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class NetworkListenerImpl @Inject constructor() : NetworkListener {
    private val _isConnected = MutableStateFlow(true)
    override val isConnected: Flow<Boolean>
        get() = _isConnected

    override fun onAvailable() {
        _isConnected.update { true }
    }

    override fun onUnavailable() {
        _isConnected.update { false }
    }
}
