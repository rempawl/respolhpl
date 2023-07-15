package com.example.respolhpl.utils

import app.cash.turbine.Event
import app.cash.turbine.ReceiveTurbine
import kotlinx.coroutines.coroutineScope
import org.junit.jupiter.api.Assertions.assertEquals

suspend fun <T> ReceiveTurbine<T>.assertItemEquals(expected: T) = coroutineScope {
    assertEquals(expected, awaitItem())
}

suspend fun <T, R> ReceiveTurbine<T>.assertItemEquals(expected: R, actual: T.() -> R) =
    coroutineScope {
        assertEquals(expected, actual(awaitItem()))
    }

suspend fun <T> ReceiveTurbine<T>.assertItemWith(assertions: T.() -> Unit) = coroutineScope {
    awaitItem().assertions()
}

suspend fun <T> ReceiveTurbine<T>.assertLatestItemEquals(expected: T) = coroutineScope {
    assertEquals(expected, expectMostRecentItem())
}

suspend fun <T, R> ReceiveTurbine<T>.assertLatestItemEquals(expected: R, actual: T.() -> R) =
    coroutineScope {
        assertEquals(expected, actual(expectMostRecentItem()))
    }

suspend fun <T> ReceiveTurbine<T>.assertLatestItemWith(assertions: T.() -> Unit) = coroutineScope {
    expectMostRecentItem().assertions()
}

suspend fun <T> ReceiveTurbine<T>.assertItems(vararg expected: T) = coroutineScope {
    for (value in expected) assertItemEquals(value)
}

suspend fun <T> ReceiveTurbine<T>.expectNoEventsAndCancel() {
    expectNoEvents()
    cancel()
}

suspend fun <T> ReceiveTurbine<T>.cancelAndConsumeRemainingItems(): List<T> {
    return cancelAndConsumeRemainingEvents()
        .filterIsInstance<Event.Item<T>>()
        .map { it.value }
}
