package com.example.scrollbooker.entity.permission.domain.model

import android.net.Uri

data class MediaVideo(
    val id: Long,
    val uri: Uri,
    val displayName: String?,
    val durationMs: Long,
    val dateAddedSec: Long,
    val sizeBytes: Long
)