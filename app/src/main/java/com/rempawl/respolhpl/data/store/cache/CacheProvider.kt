package com.rempawl.respolhpl.data.store.cache

import kotlin.reflect.KType

interface CacheProvider {
    fun <T : Any> getCacheForKey(key: String, type: KType): Cache<T>
    fun <T : Any> getAllCaches(cachePrefix: String?, type: KType): List<Cache<T>>
}