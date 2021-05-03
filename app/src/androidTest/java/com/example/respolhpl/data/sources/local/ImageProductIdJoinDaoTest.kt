package com.example.respolhpl.data.sources.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.respolhpl.data.product.entity.ImageProductIdJoin
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ImageProductIdJoinDaoTest {
    private lateinit var dataBase: AppDataBase
    private lateinit var dao: ImageProductIdJoinDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(ctx, AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = dataBase.imageProductIdJoinDao()
    }

    @After
    fun close() {
        dataBase.close()
    }

    @Test
    fun insertAndGetOne() {
        runBlockingTest {
            val join = ImageProductIdJoin(1, 2)
            dao.insert(join)
            val res = dao.getAllJoins()
            assertTrue(res.size == 1)
            assertThat(res.first(),`is`(join))
        }
    }

    @Test
    fun insertAndGetFew() {
        runBlockingTest {
            val joins = listOf(ImageProductIdJoin(1, 2),ImageProductIdJoin(1, 3),ImageProductIdJoin(2, 2))
            dao.insert(joins)
            val res = dao.getAllJoins()
            assertThat(res,`is`(joins))
        }

    }

}