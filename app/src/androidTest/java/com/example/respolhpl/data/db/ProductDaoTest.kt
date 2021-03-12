package com.example.respolhpl.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.respolhpl.utils.FakeData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ProductDaoTest {
    private lateinit var dataBase: AppDataBase
    private lateinit var dao: ProductDao

    private val products = FakeData.productEntities

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(ctx, AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = dataBase.productDao()
    }

    @After
    fun tearDown() {
        dataBase.close()
    }

    @Test
    fun insertAndGetProduct() {
        runBlocking {
            val prod = products.first()
            dao.insert(prod)
            val res = dao.getAllProducts().first().first()
            assertThat(res, `is`(prod))
        }
    }
    @Test
    fun insertAndGetProductById(){
        runBlocking {
            val prod = products.first()
            dao.insert(prod)
            val res = dao.getProductById(prod.id).first()
            assertThat(res,`is`(prod))
        }
    }
}