package com.example.respolhpl

import com.example.respolhpl.utils.DispatchersProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher

@ExperimentalCoroutinesApi
class TestDispatchersProvider : DispatchersProvider {

    val test = StandardTestDispatcher()
    override val io: CoroutineDispatcher
        get() = test
    override val main: CoroutineDispatcher
        get() = test
    override val default: CoroutineDispatcher
        get() = test
    override val unconfined: CoroutineDispatcher
        get() = test
}