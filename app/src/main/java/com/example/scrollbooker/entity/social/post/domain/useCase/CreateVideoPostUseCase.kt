package com.example.scrollbooker.entity.social.post.domain.useCase

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareDirectUploadRequest
import com.example.scrollbooker.entity.social.cloudflare.domain.repository.CloudflareRepository
import com.example.scrollbooker.entity.social.post.data.remote.CreatePostRequest
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

class CreateVideoPostUseCase @Inject constructor(
    private val cloudflareRepository: CloudflareRepository,
    private val postsRepository: PostRepository,
    private val context: Context
) {
    private val MAX_VIDEO_DURATION_MS = 60_000L

    suspend operator fun invoke(
        videoUri: Uri,
        description: String?,
        businessOrEmployeeId: Int?,
        isVideoReview: Boolean,
        videoReviewMessage: String?,
        rating: Int?,
        onProgress: (Double) -> Unit
    ): Result<Unit> {
        return runCatching {
            val durationMs = withContext(Dispatchers.IO) {
                getVideoDuration(videoUri)
            }

            if (durationMs > MAX_VIDEO_DURATION_MS) {
                throw IllegalArgumentException(
                    "The video exceeds the maximum allowed duration of 30 seconds."
                )
            }

            val direct = cloudflareRepository.getUploadUrl(
                CloudflareDirectUploadRequest()
            )

            cloudflareRepository.uploadVideo(
                uploadUrl = direct.uploadUrl,
                videoUri = videoUri,
                onProgress = onProgress
            )

            postsRepository.createPost(
                CreatePostRequest(
                    description = description,
                    provider = "cloudflare_stream",
                    providerUid = direct.providerUid,
                    linkedProductIds = emptyList(),
                    businessOrEmployeeId = businessOrEmployeeId,
                    isVideoReview = isVideoReview,
                    videoReviewMessage = videoReviewMessage,
                    rating = rating
                )
            )
        }
    }

    private fun getVideoDuration(uri: Uri): Long {
        val retriever = MediaMetadataRetriever()
        return try {
            retriever.setDataSource(context, uri)
            val time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            time?.toLongOrNull() ?: 0L
        } catch (e: Exception) {
            0L
        } finally {
            retriever.release()
        }
    }
}
