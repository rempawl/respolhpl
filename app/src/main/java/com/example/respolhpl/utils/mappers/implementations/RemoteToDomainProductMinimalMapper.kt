package com.example.respolhpl.utils.mappers.implementations

import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.utils.mappers.Mapper
import javax.inject.Inject

class RemoteToDomainProductMinimalMapper @Inject constructor() :
    Mapper<RemoteProductMinimal, ProductMinimal> {
    override fun map(from: RemoteProductMinimal): ProductMinimal {
        return ProductMinimal.from(from)
    }
}