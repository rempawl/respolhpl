package com.example.respolhpl.data.store

import com.dropbox.android.external.store4.SourceOfTruth
import com.example.respolhpl.data.store.cache.ApiCacheProvider
import com.example.respolhpl.data.store.cache.Cache
import com.example.respolhpl.data.store.cache.CacheProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Inject

abstract class TypeReference<T> : Comparable<TypeReference<T>> {
    val type: Type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]

    override fun compareTo(other: TypeReference<T>) = 0
}

class SOTFactory @Inject constructor(
    cacheProvider: ApiCacheProvider
) : DiskSOTFactory(cacheProvider)

open class DiskSOTFactory(val cacheProvider: CacheProvider) {

    inline fun <Key, reified WriteData, ReadData> create(
        cacheKeyPrefix: String,
        noinline mapper: suspend (WriteData) -> ReadData
    ) = DiskSourceOfTruth<Key, WriteData, ReadData>(
        cacheProvider,
        cacheKeyPrefix,
        object : TypeReference<WriteData>() {}.type,
        mapper
    )

    inline fun <Key, reified WriteData> create(
        cacheKeyPrefix: String,
    ) = DiskSourceOfTruth<Key, WriteData, WriteData>(
        cacheProvider,
        cacheKeyPrefix,
        object : TypeReference<WriteData>() {}.type,
        mapper = { writeData -> writeData!! }
    )
}

class DiskSourceOfTruth<Key, CacheModel, OutputModel>(
    private val cacheProvider: CacheProvider,
    private val cacheKeyPrefix: String,
    private val type: Type,
    private val mapper: suspend (CacheModel) -> OutputModel
) : SourceOfTruth<Key, CacheModel, OutputModel> {

    private fun getCacheKey(key: Key) = "${cacheKeyPrefix}_$key"

    private fun getCache(key: Key): Cache<CacheModel> =
        cacheProvider.getCacheForKey(getCacheKey(key), type)

    override suspend fun delete(key: Key) {
        getCache(key).clearCache()
    }

    override suspend fun deleteAll() {
        cacheProvider.getAllCaches<OutputModel>(cacheKeyPrefix, type).forEach {
            it.clearCache()
        }
    }

    override fun reader(key: Key): Flow<OutputModel?> {
        return flow {
            emit(get(key))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun get(key: Key): OutputModel? =
        withContext(Dispatchers.IO) {
            getCache(key).readFromCache()?.let {
                mapper(it)
            }
        }

    override suspend fun write(key: Key, value: CacheModel) {
        withContext(Dispatchers.IO) {
            getCache(key).writeToCache(value)
        }
    }
}
