package com.example.respolhpl.bindingAdapters

import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
import androidx.viewpager2.widget.ViewPager2
import com.example.respolhpl.R
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.domain.Product


object ProductBindingAdapters {


    @JvmStatic
    fun ViewPager2.setPage(page: Int?) {
        if (page == null) return
        if (currentItem != page) {
            setCurrentItem(page, true)
        }
    }


    @JvmStatic
    fun TextView.bindProductName(result: Result<*>) {
        result.checkIfIsSuccessAndType<Product>()?.let { product ->
            setTextIfItsDifferent(product.name)
        }
    }

    @JvmStatic
    fun TextView.bindProductDescription(result: Result<*>) {
        result.checkIfIsSuccessAndType<Product>()?.let { product ->
            setTextIfItsDifferent(
                HtmlCompat.fromHtml(product.description, FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
                    .toString()
            )
        }
    }


    @JvmStatic
    fun TextView.bindProductPrice(result: Result<*>) {
        result.checkIfIsSuccessAndType<Product>()?.let { product ->
            setTextIfItsDifferent(context.getString(R.string.price, product.price))
        }
    }

    private fun TextView.setTextIfItsDifferent(value: String) {
        if (text != value) {
            text = value
        }
    }


}
