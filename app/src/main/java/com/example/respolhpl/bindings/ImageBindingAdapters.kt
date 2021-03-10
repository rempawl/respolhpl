package com.example.respolhpl.bindings

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.respolhpl.R

object ImageBindingAdapters {
    private val opts: RequestOptions
        get() = RequestOptions().placeholder(R.drawable.loading)
            .error(R.drawable.ic_baseline_error_24)

    @JvmStatic
    @BindingAdapter("loadThumbnail")
    fun ImageView.loadThumbnail(src: String?) {
        src?.let {
            Glide.with(this)
                .load(src)
                .apply(opts)
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    fun ImageView.loadImage(src: String?) {
        src?.let {
            Glide.with(this)
                .load(src)
                .apply(opts)
                .into(this)
        }
    }
}