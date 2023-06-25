package com.example.respolhpl.data.store

import android.content.Context
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.IOException
import java.lang.reflect.Type
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

interface Cache<T> {
    fun readFromCache(): T?
    fun writeToCache(data: T?)
    fun clearCache()
}

interface CacheProvider {
    fun <T> getCacheForKey(key: String, type: Type): Cache<T>
    fun <T> getAllCaches(cachePrefix: String?, type: Type): List<Cache<T>>
}

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
class JsonDiskCache<T>(
    moshi: Moshi,
    type: Type,
    private val cacheFile: () -> File,
) : Cache<T> {
    private val file: File by lazy { cacheFile() }
    private val lock = Any()
    private val adapter = moshi.adapter<T>(type)

    override fun readFromCache(): T? {
        synchronized(lock) {
            return try {
                createCacheIfNeeded()
                file.source().buffer().use { source ->
                    adapter.fromJson(source)
                }
            } catch (e: Exception) {
                when (e) {
                    is IOException, is JsonDataException -> {
                        return null
                    }
                    else -> throw(e)
                }
            }
        }
    }

    override fun writeToCache(data: T?) {
        synchronized(lock) {
            createCacheIfNeeded()
            file.sink().buffer().use { sink ->
                sink.write(adapter.toJson(data).toByteArray())
            }
            // Note The approach below doesn't work for some reason
            // adapter.toJson(fileOutputStream.sink().buffer(), data)
        }
    }

    override fun clearCache() {
        synchronized(lock) {
            if (file.exists()) {
                file.delete()
            }
        }
    }

    private fun createCacheIfNeeded() {
        if (!file.exists()) {
            file.createNewFile()
        }
    }
}

@Singleton
class ApiCacheProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    moshi: Moshi,
) : DiskCacheProvider(context, moshi, "api_responses")
