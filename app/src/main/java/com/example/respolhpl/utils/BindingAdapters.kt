package com.example.respolhpl.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.respolhpl.R

fun x() {

}

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("loadThumbnail")
    fun ImageView.loadThumbnail(src: String?) {
        src?.let {
            val opts = RequestOptions().placeholder(R.drawable.loading)
                .error(R.drawable.ic_baseline_error_24)

            Glide.with(this)
                .load(src)
                .override(THUMB_WIDTH, THUMB_HEIGHT)
                .centerCrop()
                .apply(opts)
                .into(this)
        }
    }

    @JvmStatic
    @BindingAdapter("loadImage")
    fun ImageView.loadImage(src: String) {
        val opts = RequestOptions().placeholder(R.drawable.loading)
            .error(R.drawable.ic_baseline_error_24)

        Glide.with(this)
            .load(src)
            .override(IMG_HEIGHT)
            .centerCrop()
            .apply(opts)
            .into(this)

    }

    @JvmStatic
    @BindingAdapter("showWhen")
    fun View.showWhen(shouldShow: Boolean) {
        visibility = if (shouldShow) VISIBLE else GONE
    }

    private const val THUMB_WIDTH = 500
    private const val THUMB_HEIGHT = 600
    private const val IMG_HEIGHT = 1000
}