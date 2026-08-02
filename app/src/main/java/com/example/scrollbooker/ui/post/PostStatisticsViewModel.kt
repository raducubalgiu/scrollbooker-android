package com.example.scrollbooker.ui.post

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PostStatisticsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val postId: Int = savedStateHandle["postId"] ?: error("Missing postId")
}