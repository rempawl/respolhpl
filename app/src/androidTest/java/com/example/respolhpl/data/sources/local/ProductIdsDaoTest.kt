package com.example.respolhpl.data.sources.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.respolhpl.data.product.entity.ProductIdEntity
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ProductIdsDaoTest {
    private lateinit var dataBase: AppDataBase
    private lateinit var prodDao: ProductIdsDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(ctx, AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        prodDao = dataBase.productIdDao()
    }

    @After
    fun close() {
        dataBase.close()
    }

    @Test
    fun insertAndGetOne() {
        runBlockingTest {
            val prod1 = ProductIdEntity(1)
            prodDao.insert(prod1)
            val res = prodDao.getAllIds().first()
            assertTrue(res.size == 1)
            assertThat(res.first(), `is`(prod1))
        }
    }

    @Test
    fun insertAndGetFew() {
        runBlockingTest {
            val prod1 = ProductIdEntity(1)
            val prod2 = ProductIdEntity(2)
            val prod3 = ProductIdEntity(3)
            val prods = listOf(prod1, prod2, prod3)
            prodDao.insert(prods)

            val res = prodDao.getAllIds().first()
            assertThat(res, `is`(prods))

        }
    }

}