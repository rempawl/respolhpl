package com.rempawl.respolhpl.data.model.domain

import androidx.annotation.Keep
import com.rempawl.respolhpl.data.model.domain.details.Product


@Keep
data class CartProduct(
    val product: Product,
    val quantity: Int
)
