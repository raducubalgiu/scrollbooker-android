package com.example.scrollbooker.entity.social.cloudflare.domain.model

data class CloudflareUploadResponse(
    val result: CloudflareUploadResult,
    val success: Boolean
)

data class CloudflareUploadResult(
    val uid: String,
    val preview: String,
    val thumbnail: String,
    //val readyToStream: String,
    //val status: CloudflareUploadStatus
)

data class CloudflareUploadStatus(
    val state: String
)