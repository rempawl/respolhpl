package com.example.respolhpl.data.product.remote

import com.squareup.moshi.Json

@Json(name = "product")
data class RemoteProduct(
    val id: Int,
    val name: String,
    val stock_quantity: Int,
//    val shipping_class_id: Int,
    val price: Double,
    val images: List<ImageRemote>,
//    val categories: List<RemoteCategory>,
//    val tags : List<RemoteTag>,
    val description: String
)

