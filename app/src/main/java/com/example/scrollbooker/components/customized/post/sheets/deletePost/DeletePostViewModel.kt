package com.example.scrollbooker.components.customized.post.sheets.deletePost

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.scrollbooker.R
import com.example.scrollbooker.components.customized.post.PostInteractionStore
import com.example.scrollbooker.core.snackbar.SnackBarType
import com.example.scrollbooker.core.snackbar.SnackBarUiEvent
import com.example.scrollbooker.core.snackbar.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class DeletePostViewModel @Inject constructor(
    private val postInteractionStore: PostInteractionStore
): ViewModel() {
    private val _isDeleting = MutableStateFlow(false)
    val isDeleting = _isDeleting.asStateFlow()

    private val _events = MutableSharedFlow<SnackBarUiEvent.Show>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events = _events.asSharedFlow()

    fun deletePost(postId: Int, onSuccess: () -> Unit) {
        if (_isDeleting.value) return

        _isDeleting.value = true

        viewModelScope.launch {
            postInteractionStore.deletePost(postId)
                .onSuccess {
                    _isDeleting.value = false
                    onSuccess()
                }
                .onFailure { e ->
                    _isDeleting.value = false
                    Timber.tag("Post").e(e, "ERROR: on deleting post $postId")
                    _events.tryEmit(
                        SnackBarUiEvent.Show(
                            message = UiText.Resource(R.string.deletePostFailed),
                            type = SnackBarType.ERROR
                        )
                    )
                }
        }
    }
}
