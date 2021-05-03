package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.*
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.data.sources.repository.imagesCache.ImagesSource
import com.example.respolhpl.data.sources.repository.paging.ProductPagingSourceImpl
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.fakes.FakeRemoteDataSource
import com.example.respolhpl.fakes.TimeoutFakeDataSource
import com.example.respolhpl.utils.mappers.ProductsMinimalListMapper
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {
    lateinit var repositoryImpl: ProductRepositoryImpl
    lateinit var source: ImagesSource
    var remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    val dispatchersProvider = TestDispatchersProvider()
    var factory = ProductsPagerFactoryImpl(
        ProductPagingSourceImpl(
            remoteDataSource,
            ProductsMinimalListMapper()
        )
    )


    @get:Rule
    val coroutineTestRule = CoroutineTestRule(dispatchersProvider.test)

    @Before
    fun setup() {
        source = mock { onBlocking {  }}
        repositoryImpl = ProductRepositoryImpl(
            remoteDataSource, source, dispatchersProvider, factory
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
        }
    }

    @Test
    fun getProductByIdWithTimeout() {
        repositoryImpl =
            ProductRepositoryImpl(TimeoutFakeDataSource(), source, dispatchersProvider, factory)
        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductById(134).first()
            assertTrue(res.isError)
        }
    }


}