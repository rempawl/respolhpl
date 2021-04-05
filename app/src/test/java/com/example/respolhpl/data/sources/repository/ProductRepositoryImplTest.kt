package com.example.respolhpl.data.sources.repository

import androidx.paging.PagingData
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.FakeData
import com.example.respolhpl.FakeRemoteDataSource
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert
import org.junit.Assert.*
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

    }

    @Test
    fun getProducts() {
        coroutineTestRule.runBlockingTest {
            val data = repositoryImpl.getProducts().first()
            val exp = PagingData.from(FakeData.minimalProducts)
//            assertThat(data, `is`(exp)) todo

        }

    }

}