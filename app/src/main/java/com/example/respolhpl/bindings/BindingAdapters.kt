package com.example.respolhpl.bindings

import android.content.Context
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.core.text.HtmlCompat.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.example.respolhpl.R
import com.example.respolhpl.data.Result
import com.example.respolhpl.data.product.Product
import com.example.respolhpl.data.product.ProductMinimal


object BindingAdapters {
    @BindingAdapter("orderQuantity")
    @JvmStatic
    fun setOrderQuantity(view: EditText, value: String) {
        view.setText(value)
    }

    @InverseBindingAdapter(attribute = "orderQuantity")
    @JvmStatic
    fun getOrderQuantity(editText: EditText): String {
        return editText.text.toString()
    }

    @BindingAdapter("orderQuantityAttrChanged")
    @JvmStatic
    fun setListener(view: EditText, listener: InverseBindingListener?) {
        view.onFocusChangeListener = View.OnFocusChangeListener { focusedView, hasFocus ->
            focusedView as TextView
            if (hasFocus) {
                focusedView.text = ""
            } else {
                listener?.onChange()
            }
        }
    }

    @BindingAdapter("hideKeyboardOnInputDone")
    @JvmStatic
    fun hideKeyboardOnInputDone(view: EditText, enabled: Boolean) {
        if (!enabled) return
        val listener = TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                view.clearFocus()
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                        as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
            false
        }
        view.setOnEditorActionListener(listener)
    }


    @JvmStatic()
    @BindingAdapter("clearTextOnFocus")
    fun EditText.clearTextOnFocus(enabled: Boolean) {
        if (!enabled) return
        onFocusChangeListener = View.OnFocusChangeListener { view, isFocused ->

        }
    }

    @JvmStatic
    @BindingAdapter("bindQuantity")
    fun TextView.bindProductQuantity(result: Result<*>) {
        checkIfIsSuccessAndProduct(result)?.let { product ->
            text = context.getString(R.string.quantity, product.quantity)
        }
    }

    @JvmStatic
    @BindingAdapter("bindProductName")
    fun TextView.bindProductName(result: Result<*>) {
        checkIfIsSuccessAndProduct(result)?.let { product ->
            text = product.name
        }
    }

    @JvmStatic
    @BindingAdapter("bindProductDescription")
    fun TextView.bindProductDescription(result: Result<*>) {
        checkIfIsSuccessAndProduct(result)?.let { product ->
            text = HtmlCompat.fromHtml(product.description, FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH)
        }
    }

    private fun checkIfIsSuccessAndProduct(result: Result<*>): Product? {
        return if (result.isSuccess) {
            result as Result.Success
            check(result.data is Product) { "data type  should be product " }
            result.data
        } else null
    }

    @JvmStatic
    @BindingAdapter("bindProductPrice")
    fun TextView.bindProductPrice(result: Result<*>) {
        checkIfIsSuccessAndProduct(result)?.let { product ->
            text = context.getString(R.string.price, product.price)
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