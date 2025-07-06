package com.example.scrollbooker.screens.inbox.employmentRequestAccept
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class EmploymentRequestAcceptViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val employmentId: Int = savedStateHandle["employmentId"] ?: error("Missing employmentId")

    fun respondToRequest() {
        Timber.tag("Employment ID").e("$employmentId")
    }
}