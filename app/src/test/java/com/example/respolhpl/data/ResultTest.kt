package com.example.respolhpl.data

import com.example.respolhpl.FakeData
import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class ResultTest {

    lateinit var result: Result<*>
    val product = FakeData.resultSuccessProduct.data

    @Test
    fun whenSuccessAndProduct() {
        result = Result.Success(product)
        val res = result.checkIfIsSuccessAndType<Product>()
        assertThat(res, `is`(product))
        assertTrue(result.isSuccess)
        assertFalse(result.isError)
        assertFalse(result.isLoading)
    }

    @Test(expected = IllegalStateException::class)
    fun checkIfIsSuccessAndTypeFails() {
        result = Result.Success(Image("test"))
        val res = result.checkIfIsSuccessAndType<Product>()
    }

    @Test
    fun whenLoading() {
        result = Result.Loading
        val res = result.checkIfIsSuccessAndType<Product>()
        assertNull(res)
        assertTrue(result.isLoading)
        assertFalse(result.isSuccess)
        assertFalse(result.isError)
    }


    @Test
    fun whenError() {
        result = Result.Error(Exception("404"))
        val res = result.checkIfIsSuccessAndType<Product>()
        assertNull(res)
        assertTrue(result.isError)
        assertFalse(result.isSuccess)
        assertFalse(result.isLoading)

    }
}