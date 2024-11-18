package com.rempawl.respolhpl.productDetails

import com.rempawl.respolhpl.FlowResultStateCaseWithProgress
import com.rempawl.respolhpl.data.model.domain.Images
import com.rempawl.respolhpl.data.model.domain.details.ProductDetails
import com.rempawl.respolhpl.data.model.domain.details.ProductVariant
import com.rempawl.respolhpl.data.usecase.GetProductDetailsUseCase
import com.rempawl.respolhpl.utils.ProgressSemaphore
import com.rempawl.respolhpl.utils.extensions.EitherResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductDetailsStateCase @Inject constructor(
    private val productDetailsFormatter: ProductDetailsFormatter,
    getProductDetailsUseCase: GetProductDetailsUseCase,
    progressSemaphore: ProgressSemaphore,
) : FlowResultStateCaseWithProgress<Int, ProductDetails, ProductDetailsState>(
    getProductDetailsUseCase,
    progressSemaphore
) {

    override fun Flow<EitherResult<ProductDetails>>.transformToState(state: ProductDetailsState): Flow<ProductDetailsState> =
        map { productDetailsEither ->
            productDetailsEither.fold(
                ifLeft = {
                    state.copy(productError = it)
                },
                ifRight = { (product, variants) ->
                    val variantItems = createVariantItems(variants)
                    val variantItem = variantItems.firstOrNull()
                    state.copy(
                        productError = null,
                        variants = variantItems,
                        currentVariant = variantItem,
                        descriptionFormatted = productDetailsFormatter
                            .formatDescription(product.description),
                        productName = product.name,
                        toolbarLabel = product.name,
                        images = Images(product.images),
                        productQuantity = product.quantity,
                        cartQuantity = 0,
                        priceFormatted = productDetailsFormatter
                            .formatPrice(variantItem?.price ?: product.price),
                        productType = product.productType
                    )
                }
            )
        }

    private fun createVariantItems(variants: List<ProductVariant>) =
        variants.map {
            VariantItem(
                id = it.id,
                quantity = it.quantity,
                price = it.price,
                attributesFormatted = productDetailsFormatter.formatAttributes(it.productAttributes)
            )
        }
}
