package com.example.scrollbooker.shared.comment.data.remote

import com.google.gson.annotations.SerializedName

data class CreateCommentDto(
    val text: String,

    @SerializedName("parent_id")
    val parentId: Int? = null
)
