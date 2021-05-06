package com.example.respolhpl.data.sources.repository

import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.imagesCache.ImagesSource
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSourceImpl
import com.example.respolhpl.data.sources.repository.paging.ProductsPagerFactory
import com.example.respolhpl.fakes.FakeRemoteDataSource
import com.example.respolhpl.fakes.TimeoutFakeDataSource
import com.example.respolhpl.utils.mappers.implementations.RemoteToDomainProductMinimalMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {
    lateinit var repositoryImpl: ProductRepositoryImpl
    lateinit var imagesSource: ImagesSource
    lateinit var remoteDataSource: RemoteDataSource
    val dispatchersProvider = TestDispatchersProvider()

    lateinit var productsPagerFactory : ProductsPagerFactory


    @get:Rule
    val coroutineTestRule = CoroutineTestRule(dispatchersProvider.test)

    @Before
    fun setup() {
        remoteDataSource = FakeRemoteDataSource()
        productsPagerFactory = ProductsPagerFactoryImpl(
            ProductPagingSourceImpl(
                remoteDataSource,
                RemoteToDomainProductMinimalMapper()
            )
        )
        imagesSource =
            mock { onBlocking { getImages(134) } doReturn FakeData.products.first().images }
        repositoryImpl = ProductRepositoryImpl(
            remoteDataSource, imagesSource, dispatchersProvider, productsPagerFactory
        )
    }

    @Test
    fun getProductById() {

        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductById(134).first { it is Result.Success }
            assertNotNull(res.checkIfIsSuccessAndType<Product>())
        }
    }

    @Test
    fun getProductByIdError() {
        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductById(-1).first()
            Assert.assertTrue(res is Result.Error)
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
            val res = repositoryImpl.getProductImages(134).first { it is Result.Success }
            assertNotNull(res.checkIfIsSuccessAndListOf<Image>())
            res.checkIfIsSuccessAndListOf<Image>()?.let { imgs ->
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