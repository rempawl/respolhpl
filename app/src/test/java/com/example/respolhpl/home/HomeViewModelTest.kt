package com.example.respolhpl.home

import androidx.lifecycle.SavedStateHandle
import androidx.paging.PagingData
import com.example.respolhpl.CoroutineTestRule
import com.example.respolhpl.fakes.FakeData
import com.example.respolhpl.data.sources.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verifyBlocking

@ExperimentalCoroutinesApi
class HomeViewModelTest {
    lateinit var repository: ProductRepository
    lateinit var savedStateHandle: SavedStateHandle

    lateinit var viewModel: HomeViewModel

    @get:Rule
    val coroutineTestRule = CoroutineTestRule(TestCoroutineDispatcher())

    @Before
    fun setup() {
        savedStateHandle = mock<SavedStateHandle>()
        repository =
            mock { onBlocking { getProducts() } doReturn flow { emit(PagingData.from(FakeData.minimalProducts)) } }
        viewModel = HomeViewModel(repository, savedStateHandle)
    }

    @Test
    fun initResult() {
        coroutineTestRule.runBlockingTest {
            val res = viewModel.result?.first()
            verifyBlocking(repository) { getProducts() }

        }
    }
}