package com.example.scrollbooker.ui.camera

import android.net.Uri

data class CameraVideoUiState(
    val selectedUri: Uri? = null,
    val selectedKey: String? = null,
    val preparingUri: Uri? = null,
    val isReady: Boolean = false,
    val error: Throwable? = null,
    val coverUri: Uri? = null,
    val coverKey: String? = null,
    val isCoverLoading: Boolean = false
)