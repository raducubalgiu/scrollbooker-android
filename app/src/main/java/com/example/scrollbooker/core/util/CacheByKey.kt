package com.example.scrollbooker.core.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn

/**
 * Creează un StateFlow reactiv cu caching în memorie pe baza cheii [K].
 * Dacă valoarea pentru cheie există în cache, o emite instant.
 * Altfel, apelează [loader], salvează în cache și emite rezultatul.
 */
fun <K : Any, V : Any> cachedByKey(
    scope: CoroutineScope,
    keyFlow: Flow<K?>,
    loader: suspend (K) -> FeatureState<V>
): Pair<StateFlow<FeatureState<V>>, suspend (K?) -> Unit> {

    val memory = mutableMapOf<K, V>()

    val state = keyFlow
        .filterNotNull()
        .distinctUntilChanged()
        .mapLatest { key ->
            memory[key]?.let { return@mapLatest FeatureState.Success(it) }
            when (val res = loader(key)) {
                is FeatureState.Success -> {
                    memory[key] = res.data
                    res
                }
                else -> res
            }
        }
        .stateIn(scope, SharingStarted.Eagerly, FeatureState.Loading)

    // refresh pentru o cheie (șterge din cache și re-triggerează fluxul)
    val refresh: suspend (K?) -> Unit = { k ->
        if (k != null) {
            memory.remove(k)
        }
    }

    return state to refresh
}