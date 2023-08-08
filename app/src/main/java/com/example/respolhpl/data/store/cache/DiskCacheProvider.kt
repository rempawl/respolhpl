package com.example.respolhpl.data.store.cache

import android.content.Context
import com.squareup.moshi.Moshi
import java.io.File
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap

open class DiskCacheProvider(
    private val context: Context,
    private val moshi: Moshi,
    private val cacheFolderName: String
) : CacheProvider {

    // It's better to cache specific caches as they are based on locking. If we created many
    // instance of cache for specific json file then locking might not work well as
    // the file could be accessed simultaneously from many places.
    private val cachesMap: ConcurrentHashMap<String, JsonDiskCache<*>> = ConcurrentHashMap()

    val cacheDir by lazy {
        File(
            "${context.filesDir.path}${File.separator}$cacheFolderName"
        ).apply { mkdirs() }
    }

    override fun <T> getCacheForKey(key: String, type: Type): Cache<T> {
        return if (cachesMap.containsKey(key)) {
            cachesMap[key] as Cache<T>
        } else {
            val keyWithoutForbiddenChars = key.replace("[\\\\/:*?\"<>|]", "")
            JsonDiskCache<T>(
                moshi,
                type,
                cacheFile = { File(cacheDir, "$keyWithoutForbiddenChars.json") }
            ).also {
                cachesMap[key] = it
            }
        }
    }

    override fun <T> getAllCaches(cachePrefix: String?, type: Type): List<Cache<T>> {
        return cacheDir
            .listFiles { _, name ->
                cachePrefix == null || name.startsWith(cachePrefix)
            }?.map { cacheFile ->
                JsonDiskCache(moshi, type) { cacheFile }
            } ?: emptyList()
    }
}