package com.example.respolhpl.data.db

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductDaoTest{
    private lateinit var dataBase: AppDataBase
    private lateinit var dao : ProductDao

    @Before
    fun setup(){
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(ctx,AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = dataBase.productDao()
    }

    @After
    fun tearDown(){
        dataBase.close()
    }

    @Test
    fun insertAndGetProduct(){


    }
}