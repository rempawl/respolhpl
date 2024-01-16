package com.rempawl.respolhpl.data.model.remote

import androidx.annotation.Keep
import com.rempawl.respolhpl.data.model.domain.ProductMinimal
import kotlinx.serialization.Serializable

@Serializable
@Keep
data class RemoteProductMinimal(
    val id: Int,
    val name: String,
    val price: Double,
    val images: List<ImageRemote>,
    val price_html: String
)

fun RemoteProductMinimal.toDomain() = ProductMinimal(
    id = id,
    name = name,
    price = price,
    thumbnailSrc = images.first().src,
    priceHtml = price_html
)

fun List<RemoteProductMinimal>.toDomain() = this.map { it.toDomain() }