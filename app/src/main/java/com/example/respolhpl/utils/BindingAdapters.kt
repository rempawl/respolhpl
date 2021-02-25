package com.example.respolhpl.utils

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.respolhpl.R
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product

fun x() {

}

object BindingAdapters {
    private val opts : RequestOptions
        get() = RequestOptions().placeholder(R.drawable.loading)
            .error(R.drawable.ic_baseline_error_24)

    @JvmStatic
    @BindingAdapter("bindProductName")
    fun TextView.bindProductName(result: Result<*>) {
        checkIfSuccessAndIsProduct(result)?.let { product ->
            text = product.name
        }
    }

    @JvmStatic
    @BindingAdapter("bindProductDescription")
    fun TextView.bindProductDescription(result: Result<*>) {
        checkIfSuccessAndIsProduct(result)?.let { product ->
            text =
                HtmlCompat.fromHtml(product.description, FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        }
    }

    private fun checkIfSuccessAndIsProduct(result: Result<*>): Product? {
        return if (result.isSuccess) {
            result as Result.Success
            check(result.data is Product) { "data type  should be product " }
            result.data
        } else null
    }

    @JvmStatic
    @BindingAdapter("bindProductPrice")
    fun TextView.bindProductPrice(result: Result<*>) {
        checkIfSuccessAndIsProduct(result)?.let { product ->
            text = context.getString(R.string.price, product.price)
        }
    }

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

    @JvmStatic
    @BindingAdapter("showWhen")
    fun View.showWhen(shouldShow: Boolean) {
        visibility = if (shouldShow) VISIBLE else GONE
    }

    private const val THUMB_WIDTH = 500
    private const val THUMB_HEIGHT = 600

}