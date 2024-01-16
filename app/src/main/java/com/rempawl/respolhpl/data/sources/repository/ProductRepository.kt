@file:OptIn(ExperimentalCoroutinesApi::class, ExperimentalTime::class)

package com.rempawl.respolhpl.data.sources.repository

import com.rempawl.respolhpl.data.model.domain.details.Product
import com.rempawl.respolhpl.data.model.domain.ProductMinimal
import com.rempawl.respolhpl.data.model.domain.details.ProductVariant
import com.rempawl.respolhpl.data.model.remote.RemoteProduct
import com.rempawl.respolhpl.data.model.remote.RemoteProductMinimal
import com.rempawl.respolhpl.data.model.remote.RemoteProductVariant
import com.rempawl.respolhpl.data.store.ResponseStore
import com.rempawl.respolhpl.list.paging.PagingParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.time.ExperimentalTime

interface ProductRepository {

    val productsDataStore: ResponseStore<PagingParam, List<RemoteProductMinimal>, List<ProductMinimal>>

    val productDataStore: ResponseStore<Int, RemoteProduct, Product>

    val productVariantsStore : ResponseStore<Int,List<RemoteProductVariant>,List<ProductVariant>>
}
