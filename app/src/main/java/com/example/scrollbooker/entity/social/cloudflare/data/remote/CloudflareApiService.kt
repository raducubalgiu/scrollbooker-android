package com.example.scrollbooker.entity.social.cloudflare.data.remote
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface CloudflareApiService {
    @POST("cloudflare/upload")
    suspend fun uploadToCloudflare(
        @Body request: CloudflareDirectUploadRequest
    ): CloudflareDirectUploadDto
}