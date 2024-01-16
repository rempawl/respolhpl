package com.rempawl.respolhpl.data.usecase

import com.rempawl.respolhpl.data.model.domain.CartProduct
import com.rempawl.respolhpl.data.sources.repository.CartRepository
import com.rempawl.respolhpl.utils.extensions.EitherResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartProductsUseCase @Inject constructor(private val repo: CartRepository) :
    FlowResultUseCase<Unit, List<CartProduct>> {

    override fun call(parameter: Unit): Flow<EitherResult<List<CartProduct>>> {
        return repo.getCart()
    }
}