package com.example.respolhpl.data.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ImageProductIdJoin
import com.example.respolhpl.data.sources.local.ImageProductIdJoinDao
import com.example.respolhpl.data.product.entity.ProductIdEntity
import com.example.respolhpl.data.sources.local.AppDataBase
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductIdsDao
import junit.framework.Assert.assertNull
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ImagesDaoTest {
    private lateinit var dataBase: AppDataBase
    private lateinit var dao: ImagesDao
    private lateinit var prodDao: ProductIdsDao
    private lateinit var joinDao: ImageProductIdJoinDao

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
        joinDao = dataBase.imageProductIdJoinDao()
    }

    @After
    fun close() {
        dataBase.close()
    }

    @Test
    fun onEmptyDb() {
        runBlockingTest {
            val res = dao.getProductImages(1).first()
            assertNull(res)
        }
    }

    @Test
    fun insertAndGetProdWithImages() {
        val imgs = createImages()
        runBlockingTest {
            val prod = ProductIdEntity(1)
            imgs.forEach {
                joinDao.insert(
                    ImageProductIdJoin(
                        productId = prod.productId,
                        it.imageId
                    )
                )
            }

            dao.insert(imgs)
            prodDao.insert(prod)
            val res = dao.getProductImages(1).first()
            MatcherAssert.assertThat(res?.images, CoreMatchers.`is`(imgs))
        }
    }

    @Test
    fun insertAndGetProdWithImagesManyToMany() {
        val imgs = createImages()

        runBlockingTest {
            val prod = ProductIdEntity(1)
            val prod2 = ProductIdEntity(2)
            imgs.forEach {
                joinDao.insert(
                    ImageProductIdJoin(
                        productId = prod.productId,
                        it.imageId
                    )
                )
            }
            joinDao.insert(ImageProductIdJoin(prod2.productId, imgs.first().imageId))

            dao.insert(imgs)
            prodDao.insert(prod)
            prodDao.insert(prod2)
            val res = dao.getProductImages(1).first()
            MatcherAssert.assertThat(res?.images, CoreMatchers.`is`(imgs))

            val res2 = dao.getProductImages(prod2.productId).first()
            MatcherAssert.assertThat(res2?.images, CoreMatchers.`is`(listOf(imgs.first())))

        }
    }

    private fun createImages(): List<ImageEntity> {
        val img = ImageEntity(1, "one")
        val img2 = ImageEntity(2, "two")
        val img3 = ImageEntity(3, "three")
        return listOf(img, img2, img3)
    }


}