package com.rempawl.respolhpl.utils

import javax.inject.Inject

class PriceFormatter @Inject constructor() {

    // todo pass currency as parameter
    fun format(price: Double, format: String = "%.2f") = String.format(format, price)
        .plus(" PLN")
}