package com.rempawl.respolhpl.data.model.domain.details

import com.rempawl.respolhpl.data.model.domain.ProductImage


@androidx.annotation.Keep
data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
    val images: List<ProductImage>,
    val thumbnailSrc: String?,
    val price: Double,
    val description: String,
    val productType: ProductType,
)
