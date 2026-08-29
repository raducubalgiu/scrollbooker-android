package com.example.scrollbooker.entity.social.comment.data.remote

import com.google.gson.annotations.SerializedName

data class CreateCommentDto(
    val text: String,

    @SerializedName("parent_id")
    val parentId: Int? = null,

    @SerializedName("reply_to_comment_id")
    val replyToCommentId: Int? = null
)
