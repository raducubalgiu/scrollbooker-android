package com.example.scrollbooker.entity.social.comment.data.remote

import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.annotations.SerializedName

data class CommentDto(
    val id: Int,
    val text: String,

    val user: CommentUserDto,

    @SerializedName("post_id")
    val postId: Int,

    @SerializedName("replies_count")
    val repliesCount: Int,

    @SerializedName("like_count")
    val likeCount: Int,

    @SerializedName("is_liked")
    val isLiked: Boolean,

    @SerializedName("liked_by_post_author")
    val likedByPostAuthor: Boolean,

    @SerializedName("parent_id")
    val parentId: Int?,

//    @SerializedName("created_at")
//    val createdAt: Instant,
)

data class CommentUserDto(
    val id: Int,
    val username: String,

    @SerializedName("fullname")
    val fullName: String,

    val avatar: String?
)