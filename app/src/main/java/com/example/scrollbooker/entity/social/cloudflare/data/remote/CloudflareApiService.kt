package com.example.scrollbooker.entity.social.cloudflare.data.remote
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface CloudflareApiService {
    @POST("cloudflare/upload")
    suspend fun uploadToCloudflare(
        @Body request: CloudflareDirectUploadRequest
    ): CloudflareDirectUploadDto

    @Multipart
    @POST
    suspend fun uploadVideo(
        @Url uploadUrl: String,
        @Part file: MultipartBody.Part
    )
}