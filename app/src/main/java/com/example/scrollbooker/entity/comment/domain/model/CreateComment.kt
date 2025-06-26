package com.example.scrollbooker.entity.comment.domain.model

data class CreateComment(
    val text: String,
    val parentId: Int? = null
)