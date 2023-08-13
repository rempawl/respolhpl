package com.example.respolhpl.utils.extensions

fun <T> List<T>.lastButOne(): T {
    return (lastIndex - 1).takeIf { it >= 0 }?.let { get(it) } ?: get(0)
}