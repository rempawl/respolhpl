package com.rempawl.respolhpl.data.model.domain

import androidx.annotation.Keep


@Keep
data class ProductMinimal(
    val id: Int,
    val name: String,
    val price: Double,
    val thumbnailSrc: String?,
    val priceHtml : String
)