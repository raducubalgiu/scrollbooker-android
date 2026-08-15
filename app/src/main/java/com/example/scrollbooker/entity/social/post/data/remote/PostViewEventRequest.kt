package com.example.scrollbooker.entity.social.post.data.remote

import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.google.gson.annotations.SerializedName

data class PostViewEventRequest(
    @SerializedName("session_id")
    val sessionId: String,

    @SerializedName("source")
    val source: PostViewSourceEnum = PostViewSourceEnum.OTHER,

    @SerializedName("watched_ms_delta")
    val watchedMsDelta: Int,

    @SerializedName("position_ms")
    val positionMs: Int,

    @SerializedName("media_duration_ms")
    val mediaDurationMs: Int? = null,

    @SerializedName("viewer_fingerprint_hash")
    val viewerFingerprintHash: String? = null
)