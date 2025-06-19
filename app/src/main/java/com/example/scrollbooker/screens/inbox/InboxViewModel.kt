package com.example.scrollbooker.screens.inbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.scrollbooker.shared.notification.domain.model.Notification
import com.example.scrollbooker.shared.notification.domain.useCase.GetNotificationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    getNotificationsUseCase: GetNotificationsUseCase
): ViewModel() {

    init {
        Timber.tag("Init Inbox").e("-> View Model Created")
    }

    val notifications: Flow<PagingData<Notification>> =
                getNotificationsUseCase().cachedIn(viewModelScope)
}