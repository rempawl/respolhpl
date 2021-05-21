package com.example.respolhpl.network

import android.net.ConnectivityManager
import android.net.Network
import javax.inject.Inject

class NetworkCallbackImpl @Inject constructor(private val networkListener: NetworkListener) :
    ConnectivityManager.NetworkCallback() {

    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        networkListener.onAvailable()
    }

    override fun onLost(network: Network) {
        super.onLost(network)
        networkListener.onUnavailable()
    }

}