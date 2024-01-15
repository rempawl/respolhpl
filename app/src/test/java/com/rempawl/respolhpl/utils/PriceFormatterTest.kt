package com.rempawl.respolhpl.utils

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PriceFormatterTest {

    @Test
    fun `when price equals 10, then formatted correctly `() = PriceFormatter().run {
        val result = this.format(10.0)

        assertEquals("10.00", result)
    }

    @Test
    fun `when price equals 10,99, then formatted correctly`() = PriceFormatter().run {
        val result = this.format(10.99)

        assertEquals("10.99", result)
    }

    @Test
    fun `given format with 1 decimal place, when price equals 10,2, then formatted correctly`() =
        PriceFormatter().run {
            val result = this.format(10.2, "%.1f")

            assertEquals("10.2", result)
        }

}