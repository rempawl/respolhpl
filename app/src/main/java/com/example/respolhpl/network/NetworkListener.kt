package com.example.respolhpl.network

import kotlinx.coroutines.flow.Flow

interface NetworkListener {
    val isConnected : Flow<Boolean>
    fun onAvailable()
    fun onUnavailable()
}
