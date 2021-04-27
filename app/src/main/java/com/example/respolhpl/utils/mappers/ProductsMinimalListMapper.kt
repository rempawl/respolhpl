package com.example.respolhpl.utils.mappers

import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import javax.inject.Inject

//todo other mappers
class ProductsMinimalListMapper @Inject constructor() :
    ListMapper<RemoteProductMinimal, ProductMinimal> {
    override fun map(src: List<RemoteProductMinimal>): List<ProductMinimal> {
        return src.map { ProductMinimal.from(it) }
    }
}