package com.rempawl.respolhpl.data.usecase

import app.cash.turbine.test
import arrow.core.left
import arrow.core.right
import com.rempawl.respolhpl.data.model.domain.details.ProductAttribute
import com.rempawl.respolhpl.data.model.domain.details.ProductDetails
import com.rempawl.respolhpl.data.model.domain.details.ProductVariant
import com.rempawl.respolhpl.data.sources.repository.ProductRepository
import com.rempawl.respolhpl.fakes.FakeData
import com.rempawl.respolhpl.utils.AppError
import com.rempawl.respolhpl.utils.assertItemEquals
import com.rempawl.respolhpl.utils.coVerifyOnce
import com.rempawl.respolhpl.utils.mockFlowResult
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class, ExperimentalCoroutinesApi::class)
class GetProductDetailsUseCaseTest {

    private val productRepository = mockk<ProductRepository>()

    private fun createSUT(): GetProductDetailsUseCase {
        return GetProductDetailsUseCase(
            productRepository = productRepository
        )
    }

    @Test
    fun `when use case called, then correct repository methods called`() = runTest {
        productRepository.mockProduct()
        productRepository.mockVariants()
        createSUT()
            .call(911)
            .test {
                coVerifyOnce {
                    productRepository.productDataStore.cacheAndFresh(911)
                    productRepository.productVariantsStore.cacheAndFresh(911)
                }
                cancelAndIgnoreRemainingEvents()
            }
    }
    @Test
    fun `when get product with variants, then correct Product Details returned `() = runTest {
        productRepository.mockProduct()
        productRepository.mockVariants()
        createSUT()
            .call(911)
            .test {
                assertItemEquals(
                    ProductDetails(
                        product = FakeData.products.first(),
                        variants = VARIANTS
                    ).right()
                )
                awaitComplete()
            }

    }

    @Test
    fun `when get product without variants, then correct Product Details returned `() = runTest {
        productRepository.mockProduct()
        productRepository.mockVariants(variants = emptyList())
        createSUT()
            .call(911)
            .test {
                assertItemEquals(
                    ProductDetails(
                        product = FakeData.products.first(),
                        variants = emptyList()
                    ).right()
                )
                awaitComplete()
            }
    }


    @Test
    fun `when variants call fails, then correct error returned `() = runTest {
        productRepository.mockProduct()
        val error = AppError()
        productRepository.mockVariants(error = error)
        createSUT()
            .call(911)
            .test {
                assertItemEquals(error.left())
                awaitComplete()
            }
    }

    @Test
    fun `when product call fails, then correct error returned `() = runTest {
        val error = AppError()
        productRepository.mockProduct(error = error)
        productRepository.mockVariants()
        createSUT()
            .call(911)
            .test {
                assertItemEquals(error.left())
                awaitComplete()
            }
    }



    private fun ProductRepository.mockProduct(error: AppError? = null) {
        every { productDataStore.cacheAndFresh(any()) }.mockFlowResult(
            response = FakeData.products.first(),
            error = error
        )
    }

    private fun ProductRepository.mockVariants(
        variants: List<ProductVariant> = VARIANTS,
        error: AppError? = null
    ) {
        every { productVariantsStore.cacheAndFresh(any()) }.mockFlowResult(
            response = variants,
            error = error
        )
    }

    private companion object {
        val VARIANTS = listOf(
            ProductVariant(
                id = 1,
                price = 10.0,
                quantity = 10,
                productAttributes = listOf(
                    ProductAttribute(
                        name = "color",
                        value = "red"
                    ),
                    ProductAttribute(
                        name = "size",
                        value = "small"
                    )

                )
            ),
            ProductVariant(
                id = 2,
                price = 20.0,
                quantity = 20,
                productAttributes = listOf(
                    ProductAttribute(
                        name = "color",
                        value = "red"
                    ),
                    ProductAttribute(
                        name = "size",
                        value = "small"
                    )
                )
            )
        )
    }


}