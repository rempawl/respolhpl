package com.example.respolhpl.data.model.remote

import com.example.respolhpl.data.model.domain.Image
import com.squareup.moshi.Json

@Json(name = "image")
data class ImageRemote(
    val src: String, val id: Int
)

fun ImageRemote.toDomain() = Image(
    src = src,
    id = id
)

fun List<ImageRemote>.toDomain(): List<Image> = this.map { it.toDomain() }
