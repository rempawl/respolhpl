package com.example.respolhpl.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.respolhpl.data.sources.local.AppDataBase
import com.example.respolhpl.data.sources.local.ProductDao
import com.example.respolhpl.utils.FakeData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
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
        runBlockingTest {
            val prod = products.first()
            dao.insert(prod)
            val res = dao.getAllProductsMinimal().first().first()
            assertThat(res, `is`(prod))
        }
    }

    @Test
    fun insertAndGetProductById() {
        runBlockingTest {
            val prod = products.first()
            dao.insert(prod)
            val res = dao.getProductById(prod.id).first()
            assertThat(res, `is`(prod))
        }
    }

    @Test
    fun insertAndGetProducts() {
        runBlockingTest {
            val prods = products
            dao.insert(prods)
            val res = dao.getAllProductsMinimal().first()
            println(res)
            assertThat(res, `is`(prods))
        }
    }

    @Test
    fun insertAndDeleteProduct() {
        runBlockingTest {
            val prod = products.first()
            dao.insert(prod)
            dao.delete(prod)
            val res  = dao.getProductById(prod.id).first()
            assert(res == null)

        }
    }

    @Test
    fun insertAndDeleteItems(){
        runBlockingTest {
            dao.insert(products)
            dao.delete(products)
            val res = dao.getAllProductsMinimal().first()
            assert(res.isEmpty())
        }
    }

}