package com.rempawl.respolhpl.cart

import com.rempawl.respolhpl.utils.PriceFormatter
import javax.inject.Inject

class CartFormatter @Inject constructor(
    private val priceFormatter: PriceFormatter
) {
    fun formatPrice(price: Double) = priceFormatter.format(price)
}