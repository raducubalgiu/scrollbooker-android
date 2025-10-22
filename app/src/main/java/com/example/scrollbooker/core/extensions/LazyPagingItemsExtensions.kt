package com.example.scrollbooker.core.extensions
import androidx.paging.compose.LazyPagingItems

fun <T: Any> LazyPagingItems<T>.getOrNull(index: Int): T? {
    return if (index in 0 until itemCount) this[index] else null
}