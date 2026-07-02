package com.example.scrollbooker.entity.social.cloudflare.domain.repository

import android.net.Uri
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareDirectUploadRequest
import com.example.scrollbooker.entity.social.cloudflare.domain.model.CloudflareDirectUpload

interface CloudflareRepository {
    suspend fun getUploadUrl(request: CloudflareDirectUploadRequest): CloudflareDirectUpload
    suspend fun uploadVideo(
        uploadUrl: String,
        videoUri: Uri,
        onProgress: (Double) -> Unit
    )
}