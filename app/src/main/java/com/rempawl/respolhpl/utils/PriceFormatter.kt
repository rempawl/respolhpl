package com.rempawl.respolhpl.utils

import java.util.Locale
import javax.inject.Inject

class PriceFormatter @Inject constructor() {

    // todo pass currency as parameter if other than PLN will be supported
    fun format(price: Double, format: String = "%.2f") =
        String.format(Locale.getDefault(), format, price)
            .plus(" PLN")
}