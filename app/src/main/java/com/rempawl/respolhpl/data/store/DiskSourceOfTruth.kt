package com.rempawl.respolhpl.data.store

import com.rempawl.respolhpl.data.store.cache.ApiCacheProvider
import com.rempawl.respolhpl.data.store.cache.Cache
import com.rempawl.respolhpl.data.store.cache.CacheProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import org.mobilenativefoundation.store.store5.SourceOfTruth
import javax.inject.Inject
import kotlin.reflect.KType
import kotlin.reflect.typeOf

class SOTFactory @Inject constructor(
    cacheProvider: ApiCacheProvider
) : DiskSOTFactory(cacheProvider)

open class DiskSOTFactory(val cacheProvider: CacheProvider) {

    inline fun <Key: Any, reified WriteData : Any, ReadData : Any> create(
        cacheKeyPrefix: String,
        noinline mapper: suspend (WriteData) -> ReadData
    ) = DiskSourceOfTruth<Key, WriteData, ReadData>(
        cacheProvider,
        cacheKeyPrefix,
        typeOf<WriteData>(),
        mapper
    )

    inline fun <Key: Any, reified WriteData : Any> create(
        cacheKeyPrefix: String,
    ) = DiskSourceOfTruth<Key, WriteData, WriteData>(
        cacheProvider,
        cacheKeyPrefix,
        typeOf<WriteData>(),
        mapper = { writeData -> writeData }
    )
}

class DiskSourceOfTruth<Key: Any, CacheModel : Any, OutputModel : Any>(
    private val cacheProvider: CacheProvider,
    private val cacheKeyPrefix: String,
    private val type: KType,
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
