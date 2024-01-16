package com.rempawl.respolhpl.utils.extensions

fun <T> List<T>.lastButOne(): T {
    return (lastIndex - 1).takeIf { it >= 0 }?.let { get(it) } ?: get(0)
}

fun <T> List<T>.addItemIf(predicate: () -> Boolean, item: T): List<T> =
    if (predicate()) this + item else this

fun <T> T.asList(): List<T> = listOf(this)