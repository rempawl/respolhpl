@file:OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)

package com.rempawl.respolhpl.data.usecase

import arrow.core.raise.either
import com.rempawl.respolhpl.data.model.domain.details.ProductDetails
import com.rempawl.respolhpl.data.sources.repository.ProductRepository
import com.rempawl.respolhpl.utils.extensions.EitherResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class GetProductDetailsUseCase @Inject constructor(private val productRepository: ProductRepository) :
    FlowResultUseCase<Int, ProductDetails> {


    override fun call(parameter: Int): Flow<EitherResult<ProductDetails>> {
        return productRepository.productDataStore.cacheAndFresh(parameter)
            .combine(
                productRepository.productVariantsStore.cacheAndFresh(parameter)
            ) { product, variants ->
                either {
                    ProductDetails(
                        product = product.bind(),
                        variants = variants.bind()
                    )
                }
            }
    }
}
