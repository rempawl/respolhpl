package com.example.respolhpl.data.sources.repository.imagesCache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.entity.ImageProductJoin
import com.example.respolhpl.data.product.entity.ProductEntity
import com.example.respolhpl.data.sources.local.ImageProductJoinDao
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductDao
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.fakes.FakeRemoteDataSource
import com.example.respolhpl.utils.mappers.ListMapperImpl
import com.example.respolhpl.utils.mappers.NullableInputListMapperImpl
import com.example.respolhpl.utils.mappers.implementations.ImageRemoteToImgMapper
import com.example.respolhpl.utils.mappers.implementations.ImgEntityToImgMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking

@ExperimentalCoroutinesApi
class ImagesSourceImplTest {
    //todo
    private lateinit var imagesDao: ImagesDao
    private lateinit var prodDao: ProductDao
    private lateinit var joinDao: ImageProductJoinDao
    private lateinit var remoteDataSource: RemoteDataSource
    lateinit var imagesSource: ImagesSourceImpl

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(TestDispatchersProvider().test)

    @Before
    fun setup() {
        remoteDataSource = FakeRemoteDataSource()
        imagesDao = mock { }
        prodDao = mock { }
        joinDao = mock { }

        imagesSource =
            ImagesSourceImpl(
                prodDao,
                imagesDao,
                joinDao,
                remoteDataSource,
                NullableInputListMapperImpl(ImgEntityToImgMapper()),
                ListMapperImpl(ImageRemoteToImgMapper())
            )
    }

    @Test
    fun saveFew() {
        coroutineTestRule.runBlockingTest {
            val imgs = createImgs()
            val product = ProductEntity(1)
            val joins = createJoins(imgs)
            imagesSource.saveImages(imgs, 1)
            verifyBlocking(imagesDao) { insert(imgs.map { ImageEntity(it.id, it.src) }) }
            verifyBlocking(prodDao) { insert(product) }

            verifyBlocking(joinDao) { insert(joins) }
        }
    }

    @Test
    fun getWithEmptyDb() {
        //todo
    }

    @Test
    fun getFromDb() {
        //todo
    }

    private fun createJoins(imgs: List<Image>) =
        imgs.map {
            ImageProductJoin(1, it.id)
        }

    private fun createImgs() = listOf(Image("one", 1), Image("two", 2), Image("three", 3))

}