package com.example.respolhpl.fakes

import com.example.respolhpl.cart.data.CartProduct
import com.example.respolhpl.cart.data.CartProductEntity
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.entity.FavProductEntity
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.data.product.remote.RemoteProductMinimal

object FakeData {
    val resultSuccessProduct = Result.Success(
        Product(
            1,
            "test",
            5,
            emptyList(),
            "src",
            22.2,
            ""
        )
    )
    val remoteProducts: List<RemoteProduct> = listOf(
        RemoteProduct(
//            tags = listOf(RemoteTag("deska do krojenia")),
//            shipping_class_id = 80,
            description = "<h2>Właściwości:</h2>\n" +
                    "<ul>\n" +
                    " \t<li>Wysoka odporność na ścieranie, zadrapania, uderzenia,zginanie</li>\n" +
                    " \t<li>Odporne na temperaturę do 230°C</li>\n" +
                    " \t<li>Łatwe do czyszczenia</li>\n" +
                    " \t<li>Bezpieczne w bezpośrednim kontakcie z żywnością</li>\n" +
                    " \t<li>Antybakteryjna w całym przekroju</li>\n" +
                    " \t<li>Światłoodporna</li>\n" +
                    " \t<li>Obustronny kolor</li>\n" +
                    "</ul>\n" +
                    "Deski do krojenia, które oferujemy Państwu są wykonane z najwyższej jakości płyt kompakt HPL marki Polyrey®.Materiał jest niezwykle sztywny i odporny mechanicznie,a dzięki zastosowaniu technologii Sanitized® są one antybakteryjne w całym przekroju przez co zwiększają bezpieczeństwo użytkowania.\n" +
                    "\n" +
                    " Są łatwe w czyszczenia, do ich wyczyszczenia wystarczy woda i gąbka lub woda z detergentem w przypadku silniejszych zabrudzeń. Nie potrzeba ich wyparzać w zmywarce, aby pozbyć  się bakterii z ich powierzchni.",
            id = 132,
            name = "Antybakteryjna deska do krojenia Orzech",
            price = 42.99,
            images = listOf(
                ImageRemote(
                    "https://i0.wp.com/respolhpl-sklep.pl/wp-content/uploads/2020/12/IMG-20201217-WA0016.jpg?fit=768%2C1212&ssl=1",
                    14
                ),
                ImageRemote(
                    "https://i2.wp.com/respolhpl-sklep.pl/wp-content/uploads/2020/12/IMG-20201217-WA0010-rotated.jpg?fit=777%2C1202&ssl=1",
                    13
                )
            ),
//            categories = listOf(RemoteCategory(RemoteCategory.ANTIBAC_BOARD_ID)),
            stock_quantity = 60,
//            shipping = ShippingRemote(11.99, 10)

        ),
        RemoteProduct(
            description = "\n" +
                    "<h1>Laminat HPL RESOPAL ®</h1>\n" +
                    "Wymiary - 3650x1320x0.8mm<br/>\n" +
                    "\n" +
                    "Struktura  EM - Smooth matt<br/>\n" +
                    "Naturalnie aksamitne w dotyku, odporne na ścieranie i odznaczające się bardzo niskim współczynnikiem odbicia wykończenie.\n" +
                    "<h2>Właściwości</h2>\n" +
                    "<ul>\n" +
                    " \t<li>Łatwa do czyszczenia powierzchnia</li>\n" +
                    " \t<li>Odporność na temperaturę do 230 C</li>\n" +
                    " \t<li>Światłotrwałe</li>\n" +
                    " \t<li>Wysoka odporność na ścieranie, zadrapania, uderzenia</li>\n" +
                    " \t<li>Bezpieczne w bezpośrednim kontakcie z żywnością</li>\n" +
                    "</ul>\n" +
                    "<h2>Zastosowania:</h2>\n" +
                    "<ul>\n" +
                    " \t<li>Fronty Mebli</li>\n" +
                    " \t<li>Blaty</li>\n" +
                    " \t<li>Panele Ścienne</li>\n" +
                    " \t<li>Przody recepcji, barów</li>\n" +
                    " \t<li>Laminat może być naklejany na płytę wiórową/mdf/hdf/regips</li>\n" +
                    "</ul>\n" +
                    "\n" +
                    "<h3></h3>\n" +
                    "<h3 class=\"p-2\" style=\"text-align: left;\">Technologia Resopal Antibacterial®</h3>\n" +
                    "<p class=\"p-2\" style=\"text-align: left;\">jest zawarta w całym przekroju materiału i jest aktywna przez cały okres eksploatacji laminatu. Bariera oparta o kationy srebra uniemożliwia podziały komórkowe,dzięki czemu nie ma możliwości namnażania się bakterii na powierzchni laminatu.</p>\n" +
                    "&nbsp;",
            id = 134,
            name = "Laminat HPL 4134 EM Missisipi Pine",
            price = 309.99,
            images = listOf(
                ImageRemote(
                    "https://i1.wp.com/respolhpl-sklep.pl/wp-content/uploads/2020/11/4134_EM.jpg?fit=1440%2C1440&ssl=1",
                    12
                )
            ),
//            categories = listOf(RemoteCategory(RemoteCategory.LAMINAT_HPL)),
//            shipping = ShippingRemote(30.0, 5),
            stock_quantity = 10,
//            tags = listOf(RemoteTag("laminat hpl")),
//            shipping_class_id = 73
        )
    )
    val products = remoteProducts.map { Product.from(it) }
    val cartEntities =
        products.map { CartProductEntity(it.id, it.name, it.price, it.thumbnailSrc, CART_QUANTITY) }
    val cartProducts = cartEntities.map { CartProduct.from(it) }

    val minimalProducts = products.map { ProductMinimal(it.id, it.name, it.price, it.thumbnailSrc) }
    val favProductEntities = remoteProducts.map { prod ->
        FavProductEntity(
            price = prod.price,
            id = prod.id,
            name = prod.name,
            thumbnailSrc = prod.images.first().src
        )
    }
    val minRemoteProds =
        remoteProducts.map { RemoteProductMinimal(it.id, it.name, it.price, it.images) }

    const val CART_QUANTITY = 2
}