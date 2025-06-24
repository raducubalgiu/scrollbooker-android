package com.example.scrollbooker.shared.comment.data.mappers
import com.example.scrollbooker.shared.comment.data.remote.CommentDto
import com.example.scrollbooker.shared.comment.domain.model.Comment
import com.example.scrollbooker.shared.user.userSocial.data.mappers.toDomain

fun CommentDto.toDomain(): Comment {
    return Comment(
        id = id,
        text = text,
        user = user.toDomain(),
        postId = postId,
        repliesCount = repliesCount,
        likeCount = likeCount,
        isLiked = isLiked,
        likedByPostAuthor = likedByPostAuthor,
        parentId = parentId,
        //createdAt = createdAt
    )
}