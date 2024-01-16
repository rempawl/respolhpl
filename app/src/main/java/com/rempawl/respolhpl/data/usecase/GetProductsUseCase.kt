package com.rempawl.respolhpl.data.usecase

import com.rempawl.respolhpl.data.model.domain.ProductMinimal
import com.rempawl.respolhpl.data.sources.repository.ProductRepository
import com.rempawl.respolhpl.list.paging.PagingParam
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class GetProductsUseCase @Inject constructor(repo: ProductRepository) :
    StoreUseCase<PagingParam, List<ProductMinimal>>(repo.productsDataStore)