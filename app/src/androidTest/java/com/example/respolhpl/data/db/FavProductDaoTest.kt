package com.example.respolhpl.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/*
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavProductDaoTest {
    private lateinit var dataBase: AppDataBase
    private lateinit var dao: FavProductDao

    private val products = FakeData.favProductEntities

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(ctx, AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = dataBase.favProductDao()
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
            val res = dao.getFavProducts().first()
            assertThat(res.size,`is`(1))
            assertThat(res.first(), `is`(prod))
        }
    }


    @Test
    fun insertAndGetProducts() {
        runBlockingTest {
            dao.insert(products)
            val res = dao.getFavProducts().first()
            println(res)
            assertThat(res, `is`(products))
        }
    }


    @Test
    fun insertAndDeleteItems() {
        runBlockingTest {
            dao.insert(products)
            dao.delete(products)
            val res = dao.getFavProducts().first()
            assert(res.isEmpty())
        }
    }

}
*/
