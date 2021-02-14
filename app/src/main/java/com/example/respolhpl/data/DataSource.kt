package com.example.respolhpl.data

import kotlinx.coroutines.Deferred

interface DataSource {
    fun getAllProductsAsync() : Deferred<String>
}