package com.example.respolhpl.utils

import app.cash.turbine.Event
import app.cash.turbine.ReceiveTurbine
import kotlinx.coroutines.coroutineScope
import org.junit.jupiter.api.Assertions.assertEquals

suspend fun <T> ReceiveTurbine<T>.assertItemEquals(expected: T) = coroutineScope {
    assertEquals(expected, awaitItem())
}

suspend fun <T> ReceiveTurbine<T>.assertLatestItemEquals(expected: T) = coroutineScope {
    assertEquals(expected, expectMostRecentItem())
}



suspend fun <T> ReceiveTurbine<T>.cancelAndConsumeRemainingItems(): List<T> {
    return cancelAndConsumeRemainingEvents()
        .filterIsInstance<Event.Item<T>>()
        .map { it.value }
}
