package com.example.respolhpl.utils

import com.example.respolhpl.BuildConfig
import timber.log.Timber

@Suppress("NOTHING_TO_INLINE")
inline fun log(message: () -> String) {
    Timber.d(debugMessage(message))
}

inline fun debugMessage(message: () -> String): String {
    return if (BuildConfig.DEBUG ) {
        return message()
    } else {
        "debug message hidden"
    }
}
