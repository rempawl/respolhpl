package com.example.respolhpl.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.respolhpl.data.product.domain.Image

fun ImageView.loadImage(img: Image) {
    Glide.with(context)
        .load(img.src)
        .into(this)
}

fun ImageView.loadImage(imgSrc: String?) {
    Glide.with(context)
        .load(imgSrc)
        .into(this)
}

