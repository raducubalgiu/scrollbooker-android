package com.example.scrollbooker.screens.auth.collectBusinessDetails.collectBusinessLocation
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectBusinessLocationViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val businessTypeId: Int = savedStateHandle["businessTypeId"] ?: error("Missing businessTypeId")
}