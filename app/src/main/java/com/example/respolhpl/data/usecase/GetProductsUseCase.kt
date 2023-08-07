package com.example.respolhpl.data.usecase

import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.sources.repository.ProductRepository
import com.example.respolhpl.data.paging.PagingParam
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class GetProductsUseCase @Inject constructor(repo: ProductRepository) :
    StoreUseCase<PagingParam, List<ProductMinimal>>(repo.productsDataStore)