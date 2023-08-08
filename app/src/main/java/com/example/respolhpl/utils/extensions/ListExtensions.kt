package com.example.respolhpl.utils.extensions

fun <T> List<T>.lastButOne(): T {
    return get(lastIndex - 1)
}