package com.example.respolhpl

import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher

@ExperimentalCoroutinesApi
class TestDispatchersProvider : DispatchersProvider {

    val test = TestCoroutineDispatcher()
    override val io: CoroutineDispatcher
        get() = test
    override val main: CoroutineDispatcher
        get() = test
    override val default: CoroutineDispatcher
        get() = test
    override val unconfined: CoroutineDispatcher
        get() = test
}