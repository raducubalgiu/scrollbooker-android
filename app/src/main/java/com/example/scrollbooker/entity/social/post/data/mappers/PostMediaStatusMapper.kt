package com.example.scrollbooker.entity.social.post.data.mappers
import com.example.scrollbooker.core.enums.MediaStatusEnum
import com.example.scrollbooker.entity.social.post.data.remote.PostMediaStatusItemDto
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.PostMediaStatus

fun PostMediaStatusItemDto.toDomain(): PostMediaStatus {
    return PostMediaStatus(
        postId = postId,
        status = MediaStatusEnum.fromKey(status),
        readyToStream = readyToStream,
        url = url,
        urlHls = urlHls,
        thumbnailUrl = thumbnailUrl,
        customCoverUrl = customCoverUrl
    )
}

fun Post.withMediaStatus(mediaStatus: PostMediaStatus?): Post {
    if (mediaStatus == null || mediaStatus.postId != id) return this
    val firstMedia = mediaFiles.firstOrNull() ?: return this

    val patchedMedia = firstMedia.copy(
        status = mediaStatus.status,
        readyToStream = mediaStatus.readyToStream,
        url = mediaStatus.url ?: firstMedia.url,
        thumbnailUrl = mediaStatus.thumbnailUrl ?: firstMedia.thumbnailUrl,
        customCoverUrl = mediaStatus.customCoverUrl ?: firstMedia.customCoverUrl
    )

    return copy(mediaFiles = listOf(patchedMedia) + mediaFiles.drop(1))
}
