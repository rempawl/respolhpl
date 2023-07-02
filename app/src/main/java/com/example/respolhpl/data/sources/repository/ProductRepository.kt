@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)

package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.ImageRemote
import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.store.ResponseStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

interface ProductRepository {

    suspend fun getProducts(): Flow<PagingData<ProductMinimal>>
//    suspend fun getProductById(id: Int): RemoteProduct
//    suspend fun getProductImages(id : Int): List<Image>

    val productDataStore: ResponseStore<Int, RemoteProduct, Product>
    val productImagesDataStore: ResponseStore<Int, List<ImageRemote>, List<Image>>

}
