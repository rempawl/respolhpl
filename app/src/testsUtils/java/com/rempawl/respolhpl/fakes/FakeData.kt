package com.rempawl.respolhpl.fakes

import com.rempawl.respolhpl.data.model.domain.ProductImage
import com.rempawl.respolhpl.data.model.domain.ProductMinimal
import com.rempawl.respolhpl.data.model.domain.details.Product
import com.rempawl.respolhpl.data.model.remote.ImageRemote
import com.rempawl.respolhpl.data.model.remote.RemoteProduct

object FakeData {
    val remoteProducts: List<RemoteProduct> = listOf(
        RemoteProduct(
            description = "Description",
            id = 132,
            name = "Product",
            price = 42.99,
            images = listOf(
                ImageRemote(
                    "testUri1",
                    14
                ),
                ImageRemote(
                    "testUrl2",
                    13
                )
            ),
            stock_quantity = 60,
            type = "variable"
        ),
        RemoteProduct(
            description = "<h1>Laminat HPL RESOPAL ®</h1>\n",
            id = 133,
            name = "Laminat HPL 4134 EM Missisipi Pine",
            price = 309.99,
            images = listOf(
                ImageRemote(
                    "testurl3",
                    12
                )
            ),
            stock_quantity = 10,
            type = "variable"

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
            type = "simple"
        )
    )

    val products: List<Product> = remoteProducts.map {
        Product(
            id = it.id,
            name = it.name,
            quantity = it.stock_quantity ?: 0,
            images = it.images.map { img -> ProductImage(img.src, img.id) },
            price = it.price,
            description = it.description,
            thumbnailSrc = it.images.first().src,
            productType = enumValueOf(it.type.uppercase())
        )
    }

    val minimalProducts = products.map {
        ProductMinimal(
            id = it.id,
            name = it.name,
            price = it.price,
            thumbnailSrc = it.thumbnailSrc,
            priceHtml = "<span>${it.price}</span>"
        )
    }

}
