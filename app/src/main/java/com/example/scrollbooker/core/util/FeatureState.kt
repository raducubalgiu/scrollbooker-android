package com.example.scrollbooker.core.util

sealed class FeatureState<out T> {
    object Loading: FeatureState<Nothing>()
    data class Success<T>(val data: T): FeatureState<T>()
    data class Error(val error: Throwable? = null): FeatureState<Nothing>()
}