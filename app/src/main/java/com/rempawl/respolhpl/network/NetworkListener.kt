package com.rempawl.respolhpl.network

import kotlinx.coroutines.flow.Flow

interface NetworkListener {
    val isConnected: Flow<Boolean>
}
