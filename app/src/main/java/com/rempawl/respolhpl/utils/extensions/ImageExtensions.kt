package com.rempawl.respolhpl.utils.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.rempawl.respolhpl.data.model.domain.ProductImage

fun ImageView.loadImage(img: ProductImage) {
    Glide.with(context)
        .load(img.src)
        .into(this)
}

fun ImageView.loadImage(imgSrc: String?) {
    Glide.with(context)
        .load(imgSrc)
        .into(this)
}
