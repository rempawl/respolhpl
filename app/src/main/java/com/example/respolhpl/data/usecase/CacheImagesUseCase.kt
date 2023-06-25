package com.example.respolhpl.data.usecase

import com.example.respolhpl.data.model.remote.ImageRemote
import com.example.respolhpl.data.model.remote.RemoteProduct
import javax.inject.Inject

class CacheImagesUseCase @Inject constructor() : AsyncUseCase<List<ImageRemote>,Unit> {
    private suspend fun cacheImages(prod: RemoteProduct) {

    }

    override suspend fun call(parameter: List<ImageRemote>) {
//        val imgs = mappers.imgRemoteToImg.map(prod.images)
//        imagesSource.saveImages(imgs, prod.id)
    }
}
