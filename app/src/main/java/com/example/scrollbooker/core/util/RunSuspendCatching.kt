package com.example.scrollbooker.core.util

import kotlin.coroutines.cancellation.CancellationException

inline fun <T> runSuspendCatching(block: () -> T): Result<T> {
    return try {
        Result.success(block())
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (throwable: Throwable) {
        Result.failure(throwable)
    }
}
