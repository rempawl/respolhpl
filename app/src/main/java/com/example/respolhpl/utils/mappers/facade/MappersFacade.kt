package com.example.respolhpl.utils.mappers.facade

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.domain.Product
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.data.product.remote.RemoteProduct
import com.example.respolhpl.utils.mappers.ListMapper
import com.example.respolhpl.utils.mappers.Mapper
import com.example.respolhpl.utils.mappers.NullableInputListMapper
import javax.inject.Inject

interface MappersFacade {
     val imgEntityToImg: NullableInputListMapper<ImageEntity, Image>
     val imgRemoteToImg: ListMapper<ImageRemote, Image>
     val prodRemoteToProd : Mapper<RemoteProduct, Product>

}

class MappersFacadeImpl @Inject constructor(
    override val imgEntityToImg: NullableInputListMapper<ImageEntity, Image>,
    override val imgRemoteToImg: ListMapper<ImageRemote, Image>,
    override val prodRemoteToProd: Mapper<RemoteProduct, Product>
) : MappersFacade