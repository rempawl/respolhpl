package com.example.respolhpl.data.sources.repository.imagesCache

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.TestDispatchersProvider
import com.example.respolhpl.data.sources.local.ImageProductIdJoinDao
import com.example.respolhpl.data.sources.local.ImagesDao
import com.example.respolhpl.data.sources.local.ProductIdsDao
import com.example.respolhpl.utils.mappers.ImageEntityListMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class ImagesCacheImplTest {
    //todo
    private lateinit var imagesDao: ImagesDao
    private lateinit var prodDao: ProductIdsDao
    private lateinit var joinDao: ImageProductIdJoinDao
    lateinit var imagesCacheImpl: ImagesCacheImpl

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val coroutineTestRule = CoroutineTestRule(TestDispatchersProvider().test)

    @Before
    fun setup() {
        imagesDao = mock {}
        prodDao = mock {}
        joinDao = mock {}
        imagesCacheImpl = ImagesCacheImpl(prodDao, imagesDao, joinDao, ImageEntityListMapper())
    }

}