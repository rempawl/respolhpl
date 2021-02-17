package com.example.respolhpl.data.sources

import kotlinx.coroutines.Deferred

interface RemoteDataSource {
    fun getAllProductsAsync() : Deferred<String>
}