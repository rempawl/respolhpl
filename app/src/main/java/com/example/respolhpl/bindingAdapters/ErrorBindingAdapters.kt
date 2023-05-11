package com.example.respolhpl.bindingAdapters

import android.content.Context
import android.widget.TextView
import com.example.respolhpl.R
import com.example.respolhpl.data.Result
import retrofit2.HttpException
import java.net.SocketTimeoutException

//todo fix
object ErrorBindingAdapters {

    fun TextView.bindError(result: Result<*>) {
        val errorMsg = createErrorMsgIfIsError(result, context) ?: ""
        if (errorMsg.isNotBlank() && text != errorMsg) {
            text = errorMsg
        }
    }

    private fun createErrorMsgIfIsError(res: Result<*>, context: Context): String? {
        return res.takeIf { it.isError }?.let { it ->
            createErrorMessage((it as Result.Error).exception, context)
        }
    }

    private fun createErrorMessage(exception: Throwable, context: Context): String =
        when (exception) {
            is HttpException -> getCodeMessage(exception.code(), context)
            is SocketTimeoutException -> context.getString(R.string.timeout_error)
            else -> context.getString(R.string.an_error_occurred)

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
