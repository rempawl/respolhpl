package com.rempawl.respolhpl.data.model.domain.details

data class ProductDetails(
    val product: Product,
    val variants: List<ProductVariant> = emptyList()
)
