package com.example.respolhpl.utils

import com.example.respolhpl.productDetails.NullProductIdError

open class DefaultError(open val throwable: Throwable? = null, open val message: String? = null) {

    override fun toString(): String {
        return "${this.javaClass.simpleName} (throwable=$throwable). Message ${throwable?.message} $message"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DefaultError

        if (throwable != other.throwable) return false
        return message == other.message
    }

    override fun hashCode(): Int {
        var result = throwable?.hashCode() ?: 0
        result = 31 * result + (message?.hashCode() ?: 0)
        return result
    }
}

fun Throwable.toDefaultError() = DefaultError(this)

fun DefaultError.getErrorMessage() = when (this) {
    is NullProductIdError -> "Error: null product id"
    else -> "Something went wrong"

}