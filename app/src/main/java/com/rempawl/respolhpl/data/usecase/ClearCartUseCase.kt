package com.rempawl.respolhpl.data.usecase

import com.rempawl.respolhpl.data.sources.repository.CartRepository
import javax.inject.Inject

class ClearCartUseCase @Inject constructor(private val repo: CartRepository) :
    ActionResultUseCase<Unit, Unit>() {

    override suspend fun doWork(parameter: Unit) {
        repo.clearCart()
    }
}
