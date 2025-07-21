package com.example.scrollbooker.entity.social.comment.data.mappers
import com.example.scrollbooker.entity.social.comment.data.remote.CommentDto
import com.example.scrollbooker.entity.social.comment.domain.model.Comment
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain

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