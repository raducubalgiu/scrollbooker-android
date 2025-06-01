package com.example.scrollbooker.core.util

import com.example.scrollbooker.R

sealed class FeatureState<out T> {
    object Loading: FeatureState<Nothing>()
    data class Success<T>(val data: T): FeatureState<T>()
    data class Error(
        val messageRes: Int = R.string.somethingWentWrong,
        val throwable: Throwable? = null
    ): FeatureState<Nothing>()
}