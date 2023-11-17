package com.example.respolhpl.data.model.domain

import androidx.annotation.Keep


@Keep
data class CartProduct(
    val product: Product,
    val quantity: Int
)
