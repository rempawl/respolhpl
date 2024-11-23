package com.rempawl.respolhpl.utils

import com.rempawl.respolhpl.productDetails.NullProductIdError

open class AppError(open val throwable: Throwable? = null, open val message: String? = null) {

    constructor(error: AppError) : this(error.throwable, error.message)

    override fun toString(): String {
        return """${this.javaClass.simpleName} (throwable=$throwable). 
            |Message ${throwable?.message} $message""".trimMargin()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AppError

        if (throwable != other.throwable) return false
        return message == other.message
    }

    override fun hashCode(): Int {
        var result = throwable?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }
}

fun Throwable.toDefaultAppError() = AppError(this)

fun AppError.getErrorMessage() = when (this) {
    is NullProductIdError -> "Error: null product id"
    else -> "Something went wrong"
}