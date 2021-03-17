package com.example.respolhpl.data.product.remote

import com.squareup.moshi.Json

@Json(name="category")
data class RemoteCategory(val id : Int){
    companion object{
        const val ANTIBAC_BOARD_ID = 21
        const val LAMINAT_HPL_ID = 12
        const val MODERNBOX_ID =-1
    }
}