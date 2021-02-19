package com.example.respolhpl.data.product.remote

import com.squareup.moshi.Json

@Json(name = "image")
data class ImageRemote(
    val src : String,
    val name: String
)

