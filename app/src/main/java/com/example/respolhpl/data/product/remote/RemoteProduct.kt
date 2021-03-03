package com.example.respolhpl.data.product.remote

import com.example.respolhpl.data.product.Shipping
import com.squareup.moshi.Json

@Json(name = "product")
data class RemoteProduct(
    val id: Long,
    val name: String,
    val quantity : Int,
    val shipping : ShippingRemote,
    val price: Double,
    val images: List<ImageRemote>,
    val categories: List<RemoteCategory>,
    val description: String
)

