package com.example.scrollbooker.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

suspend fun <T> withVisibleLoading(minLoadingMs: Long = 400L, block: suspend () -> T): T {
    val job = CoroutineScope(Dispatchers.IO).async { block() }
    delay(minLoadingMs)
    return job.await()
}