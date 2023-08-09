package com.example.respolhpl.data.model.remote

import com.example.respolhpl.data.model.domain.ProductMinimal
import com.squareup.moshi.Json

@Json(name = "minimal_product")
data class RemoteProductMinimal(
    val id: Int,
    val name: String,
    val price: Double,
    val images: List<ImageRemote>,
)

fun RemoteProductMinimal.toDomain() = ProductMinimal(
    id = id,
    name = name,
    price = price,
    thumbnailSrc = images.first().src,
)

fun List<RemoteProductMinimal>.toDomain() = this.map { it.toDomain() }