package com.example.scrollbooker.feature.notifications.presentation

import androidx.lifecycle.ViewModel
import timber.log.Timber
import javax.inject.Inject

class NotificationsViewModel @Inject constructor(): ViewModel() {
    init {
        Timber.tag("Init Notifications").e("-> View Model Created")
    }
}