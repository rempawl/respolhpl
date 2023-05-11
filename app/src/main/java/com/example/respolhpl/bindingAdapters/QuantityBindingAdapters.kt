package com.example.respolhpl.bindingAdapters

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView

object QuantityBindingAdapters {

    @JvmStatic
    fun setOrderQuantity(view: EditText, value: String) {
        if(view.text.toString() != value){
            view.setText(value)
        }
    }

    @JvmStatic
    fun getOrderQuantity(editText: EditText): String {
        return editText.text.toString()
    }

//    @JvmStatic
//    fun setListener(view: EditText, listener: InverseBindingListener?) {
//        view.onFocusChangeListener = View.OnFocusChangeListener { focusedView, hasFocus ->
//            focusedView as TextView
//            if (hasFocus) {
//                focusedView.text = ""
//            } else {
//                listener?.onChange()
//            }
//        }
//    }
//
//    @JvmStatic
//    fun hideKeyboardOnInputDone(view: EditText, enabled: Boolean) {
//        if (!enabled) return
//        val listener = TextView.OnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                view.clearFocus()
//                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE)
//                        as InputMethodManager
//                imm.hideSoftInputFromWindow(view.windowToken, 0)
//            }
//            false
//        }
//        view.setOnEditorActionListener(listener)
//    }

}
