package com.rempawl.respolhpl.data.model.domain.details

data class ProductVariant(
    val price: Double,
    val id: Int,
    val quantity: Int,
    val productAttributes: List<ProductAttribute>
)
