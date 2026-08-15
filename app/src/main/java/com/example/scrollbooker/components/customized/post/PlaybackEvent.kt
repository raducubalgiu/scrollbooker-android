package com.example.scrollbooker.components.customized.post

data class PlaybackEvent(
    val scopeKey: String,
    val postId: Int,
    val isPlaying: Boolean,
    val positionMs: Long,
    val durationMs: Long?
)