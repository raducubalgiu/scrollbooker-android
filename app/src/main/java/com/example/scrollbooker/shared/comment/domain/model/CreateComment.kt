package com.example.scrollbooker.shared.comment.domain.model

data class CreateComment(
    val text: String,
    val parentId: Int? = null
)