package com.example.scrollbooker.entity.social.comment.data.mappers

import com.example.scrollbooker.entity.social.comment.data.remote.CreateCommentDto
import com.example.scrollbooker.entity.social.comment.domain.model.CreateComment

fun CreateComment.toDto(): CreateCommentDto {
    return CreateCommentDto(
        text = text,
        parentId = parentId
    )
}