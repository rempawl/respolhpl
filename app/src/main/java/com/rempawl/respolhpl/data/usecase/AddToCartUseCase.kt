package com.rempawl.respolhpl.data.usecase

import com.rempawl.respolhpl.data.model.domain.details.ProductType
import com.rempawl.respolhpl.data.sources.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: CartRepository) :
    ActionFlowResultUseCase<AddToCartUseCase.Param, Unit>() {

    data class Param(
        val id: Int,
        val quantity: Int,
        val type: ProductType,
        val variantId: Int?
    )

    override suspend fun doWork(parameter: Param) = with(parameter) {
        repo.addProduct(
            id = id,
            quantity = quantity,
            type = type,
            variantId = variantId
        )
    }
}