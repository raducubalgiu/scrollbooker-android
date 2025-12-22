package com.example.scrollbooker.entity.social.cloudflare.data.repository

import com.example.scrollbooker.entity.social.cloudflare.data.mappers.toDomain
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareApiService
import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareDirectUploadRequest
import com.example.scrollbooker.entity.social.cloudflare.domain.model.CloudflareDirectUpload
import com.example.scrollbooker.entity.social.cloudflare.domain.repository.CloudflareRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CloudflareRepositoryImpl @Inject constructor(
    private val apiService: CloudflareApiService
): CloudflareRepository {
    override suspend fun directVideoUpload(request: CloudflareDirectUploadRequest): CloudflareDirectUpload {
        return apiService.uploadToCloudflare(request).toDomain()
    }

    override suspend fun uploadVideo(uploadUrl: String, file: File) {
        val body = file.asRequestBody("video/mp4".toMediaTypeOrNull())
        val videoPart = MultipartBody.Part.createFormData("file", file.name, body)

        return apiService.uploadVideo(uploadUrl, videoPart)
    }
}