package com.example.respolhpl.bindingAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.respolhpl.R
import com.example.respolhpl.bindingAdapters.ImageBindingAdapters.loadThumbnail

object ImageBindingAdapters {
    private val opts: RequestOptions
        get() = RequestOptions().placeholder(R.drawable.loading)
            .error(R.drawable.ic_baseline_error_24)
            .diskCacheStrategy(DiskCacheStrategy.ALL)

    @JvmStatic
    @BindingAdapter("loadThumbnail")
    fun ImageView.loadThumbnail(src: String?) {
        src?.let {
            GlideApp.with(this)
                .load(src)
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    fun ImageView.loadImage(src: String?) {
        src?.let {
            GlideApp.with(this)
                .load(src)
                .into(this)
        }
    }
}