package com.rempawl.respolhpl.productDetails

import com.rempawl.respolhpl.data.model.domain.details.ProductAttribute
import com.rempawl.respolhpl.utils.HtmlParser
import javax.inject.Inject

class ProductDetailsFormatter @Inject constructor(private val htmlParser: HtmlParser) {

    fun formatDescription(description: String) = htmlParser.parse(description)

    fun formatAttributes(attributes: List<ProductAttribute>) =
        attributes.joinToString { attr ->
            "${attr.name}: ${attr.value}"
        }

    fun formatPrice(price: Double) = price.toString()
        .plus(" PLN")
}