package com.example.scrollbooker.entity.social.post.domain.useCase
import com.example.scrollbooker.core.enums.ShareChannelEnum
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import timber.log.Timber
import javax.inject.Inject

class SharePostUseCase @Inject constructor(
    private val repository: PostRepository
) {
    suspend operator fun invoke(postId: Int, channel: ShareChannelEnum): Result<Unit> {
        return try {
            repository.sharePost(postId, channel)
            Result.success(Unit)

        } catch (e: Exception) {
            Timber.tag("Share Post").e(e, "ERROR: on Sharing post")
            Result.failure(e)
        }
    }
}