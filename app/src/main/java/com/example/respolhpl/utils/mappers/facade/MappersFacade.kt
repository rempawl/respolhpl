package com.example.respolhpl.utils.mappers.facade

import com.example.respolhpl.data.product.domain.Image
import com.example.respolhpl.data.product.entity.ImageEntity
import com.example.respolhpl.data.product.remote.ImageRemote
import com.example.respolhpl.utils.mappers.ListMapper
import com.example.respolhpl.utils.mappers.NullableInputListMapper
import javax.inject.Inject

interface MappersFacade {
     val imgEntityToImg: NullableInputListMapper<ImageEntity, Image>
     val imgRemoteToImg: ListMapper<ImageRemote, Image>

}

class MappersFacadeImpl @Inject constructor(
    override val imgEntityToImg: NullableInputListMapper<ImageEntity, Image>,
    override val imgRemoteToImg: ListMapper<ImageRemote, Image>
) : MappersFacade