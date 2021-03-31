package com.example.respolhpl.bindingAdapters

import android.content.Context
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.respolhpl.R
import com.example.respolhpl.data.Result
import retrofit2.HttpException
import java.net.SocketTimeoutException

object ErrorBindingAdapters {

    @BindingAdapter("bindError")
    fun TextView.bindError(result: Result<*>) {
        text = createErrorMsgIfIsError(result,context) ?: ""
    }

    private fun createErrorMsgIfIsError(res: Result<*>, context: Context): String? {
        return res.takeIf { it.isError  }?.let { it ->
            createErrorMessage((it as Result.Error).exception, context)
        }
    }

    private fun createErrorMessage(exception: Throwable, context: Context): String =
        when (exception) {
            is HttpException -> getCodeMessage(exception.code(), context)
            is SocketTimeoutException -> context.getString(R.string.timeout_error)
            else -> exception.message ?: ""

        }

    private fun getCodeMessage(code: Int, context: Context): String {
        return context.run {
            when (code) {
                404 -> getString(R.string.not_found_error)
                400 -> getString(R.string.bad_request_error)
                403 -> getString(R.string.forbidden_error)
                500 -> getString(R.string.server_error)
                else -> code.toString()
            }
        }
    }


}