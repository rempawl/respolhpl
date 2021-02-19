package com.example.respolhpl.data.product.remote

import com.squareup.moshi.Json

@Json(name= "product")
data class RemoteProduct(val name : String,
                         val price : Double,
                         val images : List<ImageRemote>,
                         val categories : List<RemoteCategory>
)

