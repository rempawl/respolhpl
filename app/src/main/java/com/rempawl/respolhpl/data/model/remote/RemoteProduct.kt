package com.rempawl.respolhpl.data.model.remote

import com.rempawl.respolhpl.data.model.domain.details.Product
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProduct(
    val id: Int,
    val name: String,
    val stock_quantity: Int?,
    val price: Double,
    val images: List<ImageRemote>,
    val description: String,
    val type: String,
)

fun RemoteProduct.toDomain(): Product {
    val images = images.toDomain()
    return Product(
        id = id,
        name = name,
        quantity = stock_quantity ?: 100,
        price = price,
        images = images,
        thumbnailSrc = images.firstOrNull()?.src,
        description = description,
        productType = enumValueOf(type.uppercase()),
    )
}
