package com.example.respolhpl.data.store.cache

interface Cache<T> {
    fun readFromCache(): T?
    fun writeToCache(data: T?)
    fun clearCache()
}