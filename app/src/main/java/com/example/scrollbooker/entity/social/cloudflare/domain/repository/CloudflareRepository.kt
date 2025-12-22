package com.example.scrollbooker.entity.social.cloudflare.domain.repository

import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareDirectUploadRequest
import com.example.scrollbooker.entity.social.cloudflare.domain.model.CloudflareDirectUpload
import java.io.File

interface CloudflareRepository {
    suspend fun directVideoUpload(request: CloudflareDirectUploadRequest): CloudflareDirectUpload
    suspend fun uploadVideo(uploadUrl: String, file: File)
}