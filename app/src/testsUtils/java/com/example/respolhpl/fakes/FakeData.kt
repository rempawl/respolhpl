package com.example.respolhpl.fakes

import com.example.respolhpl.data.model.domain.Image
import com.example.respolhpl.data.model.domain.Product
import com.example.respolhpl.data.model.domain.ProductMinimal
import com.example.respolhpl.data.model.remote.ImageRemote
import com.example.respolhpl.data.model.remote.RemoteProduct
import com.example.respolhpl.data.model.remote.RemoteProductMinimal

object FakeData {
    val remoteProducts: List<RemoteProduct> = listOf(
        RemoteProduct(
            description = "<h2>Właściwości:</h2>\n",
            id = 132,
            name = "Antybakteryjna deska do krojenia Orzech",
            price = 42.99,
            images = listOf(
                ImageRemote(
                    "testUri1",
                    14
                ),
                ImageRemote(
                    "testurl2",
                    13
                )
            ),
            stock_quantity = 60,

            ),
        RemoteProduct(
            description = "<h1>Laminat HPL RESOPAL ®</h1>\n",
            id = 134,
            name = "Laminat HPL 4134 EM Missisipi Pine",
            price = 309.99,
            images = listOf(
                ImageRemote(
                    "testurl3",
                    12
                )
            ),
            stock_quantity = 10,
        ),
        RemoteProduct(
            description = "<h1>Kompakt HPL RESOPAL ®</h1>\n",
            id = 134,
            name = "Kompakt HPL 4447 EM ",
            price = 1309.99,
            images = listOf(
                ImageRemote(
                    "testurl4",
                    15
                )
            ),
            stock_quantity = 100,
        )
    )
    val products = remoteProducts.map {
        Product(
            id = it.id,
            name = it.name,
            quantity = it.stock_quantity ?: 0,
            images = it.images.map { img -> Image(img.src, img.id) },
            price = it.price,
            description = it.description,
            thumbnailSrc = it.images.first().src
        )
    }
//    val cartEntities =
//        products.map { CartProductEntity(it.id, it.name, it.price, it.thumbnailSrc, CART_QUANTITY) }
//    val cartProducts = cartEntities.map { CartProduct.from(it) } todo

    val minimalProducts = products.map { ProductMinimal(it.id, it.name, it.price, it.thumbnailSrc) }
}
