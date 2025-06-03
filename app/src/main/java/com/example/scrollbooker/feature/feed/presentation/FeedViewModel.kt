package com.example.scrollbooker.feature.feed.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(): ViewModel() {
    init {
        Timber.tag("Init").e("-> Feed - View Model Created")
    }
}