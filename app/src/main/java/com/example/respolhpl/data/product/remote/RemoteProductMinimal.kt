package com.example.respolhpl.data.product.remote

import com.squareup.moshi.Json

@Json(name = "minimal_product")
data class RemoteProductMinimal(
    val id: Int,
    val name: String,
    val price: Double,
    val images: List<ImageRemote>,
)