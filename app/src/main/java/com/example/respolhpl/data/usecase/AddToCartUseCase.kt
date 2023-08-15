package com.example.respolhpl.data.usecase

import com.example.respolhpl.data.sources.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(private val repo: CartRepository) :
    ActionFlowResultUseCase<AddToCartUseCase.Param, Unit>() {

    data class Param(val id: Int, val quantity: Int)

    override suspend fun doWork(parameter: Param) {
        return repo.addProduct(parameter.id, parameter.quantity)
    }
}