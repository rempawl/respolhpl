package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import org.junit.Test

class RemoteToDomainProductMinimalMapperTest {
    lateinit var mapper: RemoteToDomainProductMinimalMapper

    @Test
    fun map() {
        mapper = RemoteToDomainProductMinimalMapper()
        val images = listOf(ImageRemote("test", 1))
        val prod = RemoteProductMinimal(1, "test", 1.23, images)
        val res = mapper.map(prod)
        val exp = ProductMinimal(
            prod.id,
            name = prod.name,
            price = prod.price,
            thumbnailSrc = images.first().src,
            false
        )
    }
}