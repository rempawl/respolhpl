package com.rempawl.respolhpl.data.model.remote

import com.rempawl.respolhpl.data.model.domain.details.ProductVariant
import kotlinx.serialization.Serializable

@Serializable
data class RemoteProductVariant(
    val price: Double,
    val id: Int,
    val stock_quantity: Int?,
    val attributes: List<RemoteAttribute>
)

fun RemoteProductVariant.toDomain() =
    ProductVariant(
        price = price,
        id = id,
        quantity = stock_quantity ?: 50,
        productAttributes = attributes.map { it.toDomain() }
    )

fun List<RemoteProductVariant>.toDomain() = map { it.toDomain() }
