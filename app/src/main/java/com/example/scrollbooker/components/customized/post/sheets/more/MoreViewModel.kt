package com.example.scrollbooker.components.customized.post.sheets.more

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(): ViewModel() {
    private val _postId = MutableStateFlow<Int?>(null)
    val postId = _postId.asStateFlow()

    fun setPostId(newPostId: Int) {
        if (_postId.value != newPostId) _postId.value = newPostId
    }
}