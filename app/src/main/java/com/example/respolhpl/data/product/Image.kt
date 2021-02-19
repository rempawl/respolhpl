package com.example.respolhpl.data.product

import android.net.Uri
import androidx.core.net.toUri
import com.example.respolhpl.data.product.remote.ImageRemote

data class Image(val name  : String,
val src : Uri){
    companion object{
        fun from(remote : ImageRemote) : Image{
            return Image(remote.name, remote.src.toUri().buildUpon().scheme("https").build())
        }
    }
}
