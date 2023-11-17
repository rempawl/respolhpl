package com.example.respolhpl.data.model.domain



@androidx.annotation.Keep
data class Product(
    val id: Int,
    val name: String,
    val quantity: Int,
    val images: List<Image>,
    val thumbnailSrc: String?,
    val price: Double,
    val description: String
)
