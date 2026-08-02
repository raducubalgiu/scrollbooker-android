package com.example.scrollbooker.entity.social.cloudflare.data.remote
import retrofit2.http.Body
import retrofit2.http.POST

interface CloudflareApiService {
    @POST("cloudflare/upload")
    suspend fun uploadToCloudflare(
        @Body request: CloudflareDirectUploadRequest
    ): CloudflareDirectUploadDto
}