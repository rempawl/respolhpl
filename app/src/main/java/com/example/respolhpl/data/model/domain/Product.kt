package com.example.respolhpl.data.model.domain


data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
//    val shipping: Shipping,
    val images: List<Image>,
//    val productCategories: List<ProductCategory>, todo
    val thumbnailSrc: String?,
    val price: Double,
    val description: String

//    val liked : Boolean = false todo
)
