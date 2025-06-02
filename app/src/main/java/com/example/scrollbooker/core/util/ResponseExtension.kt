package com.example.scrollbooker.core.util

fun <T> FeatureState<T>.getOrNull(): T? {
    return when (this) {
        is FeatureState.Success -> this.data
        else -> null
    }
}