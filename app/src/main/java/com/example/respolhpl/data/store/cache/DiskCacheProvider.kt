package com.example.respolhpl.data.store.cache

import android.content.Context
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KType

open class DiskCacheProvider(
    private val context: Context,
    private val cacheFolderName: String,
) : CacheProvider {

    // It's better to cache specific caches as they are based on locking. If we created many
    // instance of cache for specific json file then locking might not work well as
    // the file could be accessed simultaneously from many places.
    private val cachesMap: ConcurrentHashMap<String, JsonDiskCache<*>> = ConcurrentHashMap()

    private val cacheDir by lazy {
        File(context.filesDir.path, cacheFolderName).apply { mkdirs() }
    }

    override fun <T : Any> getCacheForKey(key: String, type: KType): Cache<T> {
        return if (cachesMap.containsKey(key)) {
            cachesMap[key] as Cache<T>
        } else {
            createCache<T>(key, type).also {
                cachesMap[key] = it
            }
        }
    }

    private fun <T : Any> createCache(key: String, type: KType): JsonDiskCache<T> {
        val keyWithoutForbiddenChars = key.replace("[\\\\/:*?\"<>|]", "")
        return JsonDiskCache(
            type,
            cacheFile = { File(cacheDir, "$keyWithoutForbiddenChars.json") }
        )
    }

    override fun <T : Any> getAllCaches(cachePrefix: String?, type: KType): List<Cache<T>> {
        return cacheDir
            .listFiles { _, name ->
                cachePrefix == null || name.startsWith(cachePrefix)
            }?.map { cacheFile ->
                JsonDiskCache(type) { cacheFile }
            } ?: emptyList()
    }
}
