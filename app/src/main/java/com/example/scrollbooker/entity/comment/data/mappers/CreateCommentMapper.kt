package com.example.scrollbooker.entity.comment.data.mappers
import com.example.scrollbooker.entity.comment.data.remote.CreateCommentDto
import com.example.scrollbooker.entity.comment.domain.model.CreateComment

fun CreateComment.toDto(): CreateCommentDto {
    return CreateCommentDto(
        text = text,
        parentId = parentId
    )
}