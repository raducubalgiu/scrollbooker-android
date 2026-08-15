package com.example.scrollbooker.entity.social.post.data.remote

import com.example.scrollbooker.core.enums.PostViewSourceEnum
import com.google.gson.annotations.SerializedName

data class PostViewEventBulkItemRequest(
    @SerializedName("event_id")
    val eventId: String,

    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("session_id")
    val sessionId: String,

    val source: PostViewSourceEnum,

    @SerializedName("watched_ms_delta")
    val watchedMsDelta: Int,

    @SerializedName("position_ms")
    val positionMs: Int,

    @SerializedName("media_duration_ms")
    val mediaDurationMs: Int? = null,

    @SerializedName("captured_at")
    val capturedAt: Long,

    @SerializedName("viewer_fingerprint_hash")
    val viewerFingerprintHash: String? = null
)

data class PostsViewEventsBulkRequest(
    @SerializedName("client_batch_id")
    val clientBatchId: String,

    val events: List<PostViewEventBulkItemRequest>
)

data class PostsViewEventsBulkResponse(
    val accepted: Int,
    val rejected: List<String>
)