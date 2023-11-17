package com.example.respolhpl.utils

import javax.inject.Inject

class PriceFormatter @Inject constructor() {
    fun format(price: Double, format: String = "%.2f") = String.format(format, price)
}