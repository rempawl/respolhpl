package com.example.respolhpl.bindingAdapters

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.respolhpl.R

@GlideModule
class GlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        super.applyOptions(context, builder)
        builder.setDefaultRequestOptions {
            RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_loading_black)
                .error(R.drawable.ic_baseline_error_24)
        }
    }
}