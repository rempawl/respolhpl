package com.example.respolhpl.data.sources.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule

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
//todo
    @After
    fun close() {
        dataBase.close()
    }

}