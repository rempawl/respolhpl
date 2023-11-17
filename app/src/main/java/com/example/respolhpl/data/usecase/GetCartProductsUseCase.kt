package com.example.respolhpl.data.usecase

import com.example.respolhpl.data.model.domain.CartProduct
import com.example.respolhpl.data.sources.repository.CartRepository
import com.example.respolhpl.utils.extensions.EitherResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartProductsUseCase @Inject constructor(private val repo: CartRepository) :
    FlowResultUseCase<Unit, List<CartProduct>> {

    override fun call(parameter: Unit): Flow<EitherResult<List<CartProduct>>> {
        return repo.getCart()
    }
}