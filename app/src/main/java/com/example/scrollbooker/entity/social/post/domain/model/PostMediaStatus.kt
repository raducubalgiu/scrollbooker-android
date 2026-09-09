package com.example.scrollbooker.entity.social.post.domain.model
import com.example.scrollbooker.core.enums.MediaStatusEnum

data class PostMediaStatus(
    val postId: Int,
    val status: MediaStatusEnum?,
    val readyToStream: Boolean,
    val url: String?,
    val urlHls: String?,
    val thumbnailUrl: String?,
    val customCoverUrl: String?
)
