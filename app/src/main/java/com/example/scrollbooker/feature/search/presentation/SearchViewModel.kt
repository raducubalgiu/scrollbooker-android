package com.example.scrollbooker.feature.search.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(): ViewModel() {
    init {
        Timber.tag("Init").e("-> Search - View Model Created")
    }
}