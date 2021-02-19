 package com.example.respolhpl.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

 object BindingAdapters {

     @JvmStatic
     @BindingAdapter("loadThumbnail")
     fun ImageView.loadThumbnail(src: String?) {
        src?.let {
            Glide.with(this)
                .load(src)
                .override(THUMB_WIDTH, THUMB_HEIGHT)
                .centerCrop()
                .into(this)
        }
     }

     @JvmStatic
     @BindingAdapter("showWhen")
     fun View.showWhen(shouldShow: Boolean ){
         visibility = if(shouldShow)  VISIBLE else GONE
     }
     private const val THUMB_WIDTH = 500
     private const val THUMB_HEIGHT = 600
 }