@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)

package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.RemoteProductMinimal
import com.example.respolhpl.data.paging.PagingData
import com.example.respolhpl.data.paging.PagingParam
import com.example.respolhpl.data.store.ResponseStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlin.time.ExperimentalTime

interface ProductRepository {

    val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>>

    val productDataStore: ResponseStore<Int, RemoteProduct, Product>
}
