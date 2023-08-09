package com.example.respolhpl.data.store.cache

import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import okio.buffer
import okio.sink
import okio.source
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

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