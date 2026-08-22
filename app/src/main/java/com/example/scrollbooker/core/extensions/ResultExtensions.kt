package com.example.scrollbooker.core.extensions

import com.example.scrollbooker.core.util.FeatureState

fun <T> Result<T>.toFeatureState(): FeatureState<T> =
    fold(
        onSuccess = { FeatureState.Success(it) },
        onFailure = { FeatureState.Error(it as? Exception ?: Exception(it)) }
    )