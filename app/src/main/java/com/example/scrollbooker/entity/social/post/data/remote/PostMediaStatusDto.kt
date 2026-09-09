package com.example.scrollbooker.entity.social.post.data.remote
import com.google.gson.annotations.SerializedName

data class PostMediaStatusItemDto(
    @SerializedName("post_id")
    val postId: Int,

    val status: String,

    @SerializedName("ready_to_stream")
    val readyToStream: Boolean,

    val url: String?,

    @SerializedName("url_hls")
    val urlHls: String?,

    @SerializedName("thumbnail_url")
    val thumbnailUrl: String?,

    @SerializedName("custom_cover_url")
    val customCoverUrl: String?
)
