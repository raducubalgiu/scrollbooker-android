package com.example.scrollbooker.core.util

import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems

fun <T: Any> LazyPagingItems<T>.getOrNull(index: Int): T? {
    return if (index in 0 until itemCount) this[index] else null
}

fun LazyPagingItems<*>.isTrulyEmpty(): Boolean {
    val refreshNotLoading = loadState.refresh is LoadState.NotLoading
    val appendEop = (loadState.append as? LoadState.NotLoading)?.endOfPaginationReached == true
    val prepedEop = (loadState.prepend as? LoadState.NotLoading)?.endOfPaginationReached == true
    return itemCount == 0 && refreshNotLoading && appendEop && prepedEop
}