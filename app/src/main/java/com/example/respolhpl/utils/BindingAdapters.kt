 package com.example.respolhpl.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

 object BindingAdapters {
     @BindingAdapter("loadThumbnail")
     fun ImageView.loadThumbnail(src: String) {
         Glide.with(this)
             .load(src)
             .override(300, 300)
             .centerCrop()
             .into(this)
     }

     const val THUMB_WIDTH = 400
     const val THUMB_HEIGHT = 500
 }