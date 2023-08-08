package com.example.respolhpl.utils

import io.mockk.MockKVerificationScope
import io.mockk.coVerify

fun coVerifyOnce(block: suspend MockKVerificationScope.() -> Unit) {
    coVerify(exactly = 1) { block() }
}