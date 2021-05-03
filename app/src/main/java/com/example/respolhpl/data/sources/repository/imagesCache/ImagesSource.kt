package com.example.respolhpl.data.sources.repository.imagesCache

import com.example.respolhpl.data.product.domain.Image

interface ImagesSource {
    suspend fun saveImages(res: List<Image>, prodId: Int)
    suspend fun getImages(prodId: Int): List<Image>
}