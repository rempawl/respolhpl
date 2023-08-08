package com.example.respolhpl.data.sources.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class WooCommerceApiTest {

    private lateinit var mockServer: MockWebServer
    private lateinit var api: WooCommerceApi

    @Before
    fun setup() {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        mockServer = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(WooCommerceApi::class.java)
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun getProducts() {
        runBlocking {
            enqueueResponse("products.json")
            val products =
                api.getProducts(10, 1)
            mockServer.takeRequest()

            assertThat(products.size, `is`(10))
            val product = products[0]
            assertThat(product.id, `is`(985))
            assertThat(product.name, `is`("wspornik podpórka półki Belt gtv czarny mat  2 SZT"))
            assertThat(product.price, `is`(36.99))
            assertThat(product.images.size, `is`(3))
            assertThat(
                product.images.first().src,
                `is`("https://i1.wp.com/respolhpl-sklep.pl/wp-content/uploads/2021/02/big_uchwyt-belt-czarny-1-.jpg?fit=850%2C850&ssl=1")
            )
        }
    }

    @Test
    fun getProductById() {
        val id = 956
        runBlocking {
            enqueueResponse("product_${id}.json")
            val product = api.getProductById(id)
            mockServer.takeRequest()

            assertThat(product.id, `is`(id))
            assertThat(product.name, `is`("Antybakteryjna  Deska do krojenia Orzech"))
            assertThat(product.price, `is`(42.99))
            assertThat(product.images.size, `is`(5))
            assertThat(product.stock_quantity, `is`(30))
            assertThat(
                product.images.first().src,
                `is`("https://i0.wp.com/respolhpl-sklep.pl/wp-content/uploads/2020/12/IMG-20201217-WA0016.jpg?fit=768%2C1212&ssl=1")
            )

        }
    }


    private fun enqueueResponse(fileName: String, headers: Map<String, String> = emptyMap()) {
        val inputStream = javaClass.classLoader!!
            .getResourceAsStream("api_response/$fileName")
            .source()
            .buffer()

        val mockResponse = MockResponse()
        for ((key, value) in headers) {
            mockResponse.addHeader(key, value)
        }
        mockServer.enqueue(mockResponse.setBody(inputStream.readString(Charsets.UTF_8)))

    }

}