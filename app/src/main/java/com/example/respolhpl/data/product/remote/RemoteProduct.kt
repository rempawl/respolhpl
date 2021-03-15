package com.example.respolhpl.data.product.remote

import com.squareup.moshi.Json

@Json(name = "product")
data class RemoteProduct(
    val id: Int,
    val name: String,
    val stock_quantity: Int,
//    val shipping: ShippingRemote,
    val price: Double,
    val images: List<ImageRemote>,
//    val categories: List<RemoteCategory>,
    val description: String
)

