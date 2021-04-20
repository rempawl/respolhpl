package com.example.respolhpl.cart.data.sources

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.respolhpl.FakeData
import com.example.respolhpl.data.sources.local.AppDataBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi

@RunWith(AndroidJUnit4::class)
class CartProductDaoTest {

    private lateinit var dao: CartProductDao
    private val entities = FakeData.cartEntities
    private lateinit var db: AppDataBase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(ctx, AppDataBase::class.java).allowMainThreadQueries()
            .build()
        dao = db.cartProductDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun insertOneAndGetOne() {
        runBlockingTest {
            val prod = entities.first()
            dao.insert(prod)
            val res = dao.getCartProducts().first().first()
            @Suppress("DEPRECATION")
            assertThat(res, `is`(prod))
        }
    }

    @Test
    fun insertListAndGetList() {
        runBlockingTest {
            dao.insert(entities)
            val res = dao.getCartProducts().first()
            @Suppress("DEPRECATION")
            assertThat(res, `is`(entities))
        }
    }
}
