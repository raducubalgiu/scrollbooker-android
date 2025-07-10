package com.example.scrollbooker.core.util
import kotlinx.coroutines.delay

suspend fun <T> withVisibleLoading(
    minLoadingMs: Long = 400L,
    block: suspend () -> T
): T {
    val start = System.currentTimeMillis()
    val result = block()
    val duration = System.currentTimeMillis() - start
    val remaining = minLoadingMs - duration
    if(remaining > 0) delay(remaining)
    return result
}