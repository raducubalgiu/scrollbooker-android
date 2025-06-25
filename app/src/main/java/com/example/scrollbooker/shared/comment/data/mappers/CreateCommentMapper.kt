package com.example.scrollbooker.shared.comment.data.mappers
import com.example.scrollbooker.shared.comment.data.remote.CreateCommentDto
import com.example.scrollbooker.shared.comment.domain.model.CreateComment

fun CreateComment.toDto(): CreateCommentDto {
    return CreateCommentDto(
        text = text,
        parentId = parentId
    )
}