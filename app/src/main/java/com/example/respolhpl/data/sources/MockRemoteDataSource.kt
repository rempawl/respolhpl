package com.example.respolhpl.data.sources

import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteCategory
import com.example.respolhpl.data.product.remote.RemoteCategory.Companion.ANTIBAC_BOARD_ID
import com.example.respolhpl.data.product.remote.RemoteCategory.Companion.LAMINAT_HPL
import com.example.respolhpl.data.product.remote.RemoteProduct
import kotlinx.coroutines.*
import javax.inject.Inject

class MockRemoteDataSource @Inject constructor() : RemoteDataSource {

    override fun getAllProductsAsync(): Deferred<List<RemoteProduct>> {
        return CoroutineScope(Dispatchers.IO).async {
            delay(1500)
            listOf(
                RemoteProduct(
                    id = 132,
                    name = "Antybakteryjna deska do krojenia Orzech",
                    price = 42.99,
                    images = listOf(
                        ImageRemote(
                            "https://i0.wp.com/respolhpl-sklep.pl/wp-content/uploads/2020/12/IMG-20201217-WA0016.jpg?fit=768%2C1212&ssl=1"
                        ),
                        ImageRemote("https://i2.wp.com/respolhpl-sklep.pl/wp-content/uploads/2020/12/IMG-20201217-WA0010-rotated.jpg?fit=777%2C1202&ssl=1")
                    ),
                    categories = listOf(RemoteCategory(ANTIBAC_BOARD_ID))
                ),
                RemoteProduct(
                    id = 134,
                    name = "Laminat HPL 4134 EM Missisipi Pine",
                    price = 309.99,
                    images = listOf(
                        ImageRemote(
                            "https://i1.wp.com/respolhpl-sklep.pl/wp-content/uploads/2020/11/4134_EM.jpg?fit=1440%2C1440&ssl=1"
                        )
                    ),
                    categories = listOf(RemoteCategory(LAMINAT_HPL))
                )
            )
        }
    }
}