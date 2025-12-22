package com.example.scrollbooker.entity.social.cloudflare.domain.useCase

import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareDirectUploadRequest
import com.example.scrollbooker.entity.social.cloudflare.domain.repository.CloudflareRepository
import com.example.scrollbooker.entity.social.post.data.remote.CreatePostRequest
import com.example.scrollbooker.entity.social.post.domain.repository.PostRepository
import java.io.File
import javax.inject.Inject

class CreatePostWithCloudflareUseCase @Inject constructor(
    private val cloudflareRepository: CloudflareRepository,
    private val postsRepository: PostRepository
) {
    suspend operator fun invoke(
        videoFile: File,
        description: String?
    ) {
        val direct = cloudflareRepository.directVideoUpload(
            CloudflareDirectUploadRequest(
                maxDurationSeconds = 120,
            )
        )

        cloudflareRepository.uploadVideo(direct.uploadUrl, videoFile)

        return postsRepository.createPost(
            CreatePostRequest(
                description = description,
                provider = "cloudflare_stream",
                providerUid = direct.providerUid
            )
        )
    }
}