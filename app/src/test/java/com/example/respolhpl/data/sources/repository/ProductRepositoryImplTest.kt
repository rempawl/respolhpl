package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.imagesCache.ImagesSource
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSourceImpl
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.fakes.FakeRemoteDataSource
import com.example.respolhpl.fakes.TimeoutFakeDataSource
import com.example.respolhpl.utils.mappers.ListMapperImpl
import com.example.respolhpl.utils.mappers.implementations.RemoteToDomainProductMinimalMapper
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {
    lateinit var repositoryImpl: ProductRepositoryImpl
    lateinit var imagesSource: ImagesSource
    lateinit var remoteDataSource: RemoteDataSource
    val dispatchersProvider = TestDispatchersProvider()

    lateinit var productsPagerFactory: ProductsPagerFactory

    val imgs = FakeData.products.first().images

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(dispatchersProvider.test)

    @Before
    fun setup() {
        remoteDataSource = FakeRemoteDataSource()
        productsPagerFactory = ProductsPagerFactoryImpl(
            ProductPagingSourceImpl(
                remoteDataSource,

                ListMapperImpl(RemoteToDomainProductMinimalMapper())
            )
        )
        imagesSource =
            mock { onBlocking { getImages(134) } doReturn imgs }
        repositoryImpl = ProductRepositoryImpl(
            remoteDataSource, imagesSource, dispatchersProvider, productsPagerFactory
        )
    }

    @Test
    fun getProductById() {

        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductById(134)
                .first { it.isSuccess }
            assertNotNull(res.checkIfIsSuccessAndType<Product>())
        }
    }

    @Test
    fun getProductByIdError() {
        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductById(-1).first()
            Assert.assertTrue(res.isError)
        }
    }

    @Test
    fun getProducts() {
        coroutineTestRule.runBlockingTest {
            val data = repositoryImpl.getProducts().first()
            val exp = PagingData.from(FakeData.minimalProducts)
//            assertThat(data, `is`(exp)) todo

        }

    }

    @Test
    fun getImages() {
        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductImages(134)
                .first { it.isSuccess }
            assertNotNull(res.checkIfIsSuccessAndListOf<Image>())
            res.checkIfIsSuccessAndListOf<Image>().let { imgs ->
                val exp = FakeData.products.first().images
                assertThat(imgs, `is`(exp))
            }
        }
    }

    @Test
    fun getProductByIdWithTimeout() {
        repositoryImpl =
            ProductRepositoryImpl(
                TimeoutFakeDataSource(),
                imagesSource,
                dispatchersProvider,
                productsPagerFactory
            )
        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductById(134).first()
            assertTrue(res.isError)
        }
    }


}