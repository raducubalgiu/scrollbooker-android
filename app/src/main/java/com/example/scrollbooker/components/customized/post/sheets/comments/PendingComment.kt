package com.example.scrollbooker.components.customized.post.sheets.comments

import com.example.scrollbooker.entity.social.comment.domain.model.Comment

enum class PendingStatus { SENDING, FAILED }

data class PendingComment(
    val localId: String,
    val comment: Comment,
    val parentId: Int?,
    val status: PendingStatus
)