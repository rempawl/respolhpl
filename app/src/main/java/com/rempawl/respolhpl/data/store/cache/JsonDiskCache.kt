package com.rempawl.respolhpl.data.store.cache

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.File
import java.io.FileOutputStream
import kotlin.reflect.KType

class JsonDiskCache<T : Any>(
    type: KType,
    private val cacheFile: () -> File,
) : Cache<T> {
    private val file: File by lazy { cacheFile() }
    private val lock = Any()

    private val serializer: KSerializer<Any?> = serializer(type)

    override fun readFromCache(): T? {
        synchronized(lock) {
            return try {
                createCacheIfNeeded()
                Json.decodeFromString(serializer, file.readText()) as T?
            } catch (e: Exception) {
                deleteFileIfExists()
                return null
            }
        }
    }

    override fun writeToCache(data: T) {
        synchronized(lock) {
            createCacheIfNeeded()
            FileOutputStream(file).use {
                val encodedJson = Json.encodeToString(serializer, data)
                file.writeText(encodedJson)
            }
        }
    }

    override fun clearCache() {
        synchronized(lock) {
            deleteFileIfExists()
        }
    }

    private fun createCacheIfNeeded() {
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    private fun deleteFileIfExists() {
        if (file.exists()) {
            file.delete()
        }
    }
}
