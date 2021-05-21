package com.example.respolhpl.network

import androidx.lifecycle.LiveData

interface NetworkListener {
    val isConnected : LiveData<Boolean>
    fun onAvailable()
    fun onUnavailable()
}
