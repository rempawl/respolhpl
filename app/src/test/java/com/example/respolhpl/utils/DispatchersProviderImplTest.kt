package com.example.respolhpl.utils

import kotlinx.coroutines.Dispatchers
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.assertThat
import org.junit.Test

class DispatchersProviderImplTest {
    val provider = DispatchersProviderImpl()

    @Test
    fun getIO() {
        val res = provider.io
        assertThat(res, `is`(Dispatchers.IO))
    }

    @Test
    fun getMain() {
        val res = provider.main
        assertThat(res, `is`(Dispatchers.Main))
    }

    @Test
    fun getDefault() {
        val res = provider.default
        assertThat(res, `is`(Dispatchers.Default))
    }

    @Test
    fun getUnconfined() {
        val res = provider.unconfined
        assertThat(res, `is`(Dispatchers.Unconfined))
    }
}