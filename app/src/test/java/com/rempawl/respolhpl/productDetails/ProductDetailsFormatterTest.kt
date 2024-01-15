package com.rempawl.respolhpl.productDetails

import com.rempawl.respolhpl.data.model.domain.details.ProductAttribute
import com.rempawl.respolhpl.utils.PriceFormatter
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductDetailsFormatterTest {

    private fun createFormatter() = ProductDetailsFormatter(
        mockk {
            every { parse(any()) } returns "parsed"
        },
        PriceFormatter()
    )

    @Test
    fun `when formatDescription called, then return parsed description`() {
        val result = createFormatter().formatDescription("description")

        assertEquals("parsed", result)
    }

    @Test
    fun `when format price called, then return price with PLN`() {
        val result = createFormatter().formatPrice(10.0)

        assertEquals("10.00 PLN", result)
    }

    @Test
    fun `when formatAttributes called, then return formatted attributes`() {
        val attributes = listOf(
            ProductAttribute("name1", "value1"),
            ProductAttribute("name2", "value2"),
            ProductAttribute("name3", "value3")
        )

        val result = createFormatter().formatAttributes(attributes)

        assertEquals("name1: value1, name2: value2, name3: value3", result)
    }

}