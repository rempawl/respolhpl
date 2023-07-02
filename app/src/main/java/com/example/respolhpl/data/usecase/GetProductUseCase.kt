@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.example.respolhpl.data.usecase

import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.sources.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.time.ExperimentalTime

class GetProductUseCase @Inject constructor(productRepository: ProductRepository) :
    StoreUseCase<Int, Product>(productRepository.productDataStore)
