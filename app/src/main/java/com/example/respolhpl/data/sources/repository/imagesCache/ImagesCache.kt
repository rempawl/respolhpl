package com.example.respolhpl.data.sources.repository.imagesCache

import com.example.respolhpl.data.product.domain.Image

interface ImagesCache {
    suspend fun getImages(prodId: Int, onEmpty: suspend () -> List<Image>): List<Image>
    suspend fun saveImages(res: List<Image>, prodId: Int)
}