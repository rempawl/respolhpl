package com.example.respolhpl.data.product.entity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.respolhpl.data.sources.local.AppDataBase
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductIdsDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ImagesDaoTest {
    private lateinit var dataBase: AppDataBase
    private lateinit var dao: ImagesDao
    private lateinit var prodDao: ProductIdsDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        val ctx = InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(ctx, AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = dataBase.imagesDao()
        prodDao = dataBase.productIdDao()
    }


    @Test
    fun insertAndGetProdWithImages() {
        val exp = createImages()
        runBlockingTest {
            dao.insert(exp)
            prodDao.insert(ProductIdEntity(1))
            val res = dao.getProductImages(1).first()
            assertThat(res.images, `is`(exp))
        }
    }

    private fun createImages() : List<ImageEntity>{
        val img = ImageEntity(1, 1, "one")
        val img2 = ImageEntity(1, 2, "two")
        val img3 = ImageEntity(1, 3, "three")
        return listOf(img, img2, img3)
    }


}