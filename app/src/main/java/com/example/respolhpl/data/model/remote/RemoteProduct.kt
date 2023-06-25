package com.example.respolhpl.data.model.remote

import com.example.respolhpl.data.model.domain.Product
import com.squareup.moshi.Json

@Json(name = "product")
data class RemoteProduct(
    val id: Int,
    val name: String,
    val stock_quantity: Int?,
//    val shipping_class_id: Int,
    val price: Double,
    val images: List<ImageRemote>,
//    val categories: List<RemoteCategory>,
//    val tags : List<RemoteTag>,
    val description: String
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
        description = description
    )
}

