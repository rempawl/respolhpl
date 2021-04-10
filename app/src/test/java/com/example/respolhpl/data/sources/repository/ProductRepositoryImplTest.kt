package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.FakeData
import com.example.respolhpl.FakeRemoteDataSource
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import junit.framework.Assert.assertNotNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductRepositoryImplTest {
    lateinit var repositoryImpl: ProductRepositoryImpl

    val remoteDataSource: RemoteDataSource = FakeRemoteDataSource()
    val dispatchersProvider = TestDispatchersProvider()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(dispatchersProvider.test)

    @Before
    fun setup() {
        repositoryImpl = ProductRepositoryImpl(remoteDataSource, dispatchersProvider)
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
    fun getImages(){
        coroutineTestRule.runBlockingTest {
            val res = repositoryImpl.getProductImages(134).first { it is Result.Success }
            assertNotNull(res.checkIfIsSuccessAndListOf<Image>())
        }
    }

}