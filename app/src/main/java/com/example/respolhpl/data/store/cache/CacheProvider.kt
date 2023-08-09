package com.example.respolhpl.data.store.cache

import java.lang.reflect.Type

interface CacheProvider {
    fun <T> getCacheForKey(key: String, type: Type): Cache<T>
    fun <T> getAllCaches(cachePrefix: String?, type: Type): List<Cache<T>>
}