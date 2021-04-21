package com.example.respolhpl.bindingAdapters

import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.respolhpl.R
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product


object ProductDetailsBindingAdapters {
    @JvmStatic
    @BindingAdapter("setPage")
    fun ViewPager2.setPage(page: Int?) {
        if(page == null) return
        if (currentItem != page) {
            setCurrentItem(page, true)
        }
    }

    private fun TextView.setTextIfItsDifferent(value: String) {
        if (text != value) {
            text = value
        }
    }

    @JvmStatic
    @BindingAdapter("bindQuantity")
    fun TextView.bindProductQuantity(result: Result<*>) {
        result.checkIfIsSuccessAndType<Product>()?.let { product ->
            setTextIfItsDifferent(context.getString(R.string.from_quantity, product.quantity))
        }
    }

    @JvmStatic
    @BindingAdapter("bindProductName")
    fun TextView.bindProductName(result: Result<*>) {
        result.checkIfIsSuccessAndType<Product>()?.let { product ->
            setTextIfItsDifferent(product.name)
        }
    }

    @JvmStatic
    @BindingAdapter("bindProductDescription")
    fun TextView.bindProductDescription(result: Result<*>) {
        result.checkIfIsSuccessAndType<Product>()?.let { product ->
            setTextIfItsDifferent(
                HtmlCompat.fromHtml(product.description, FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
                    .toString()
            )
        }
    }


    @JvmStatic
    @BindingAdapter("bindProductPrice")
    fun TextView.bindProductPrice(result: Result<*>) {
        result.checkIfIsSuccessAndType<Product>()?.let { product ->
            setTextIfItsDifferent(context.getString(R.string.price, product.price))
        }
    }




}