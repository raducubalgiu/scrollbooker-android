package com.example.scrollbooker.entity.social.cloudflare.data.remote

import com.google.gson.annotations.SerializedName

data class CloudflareDirectUploadRequest(
    @SerializedName("max_duration_seconds")
    val maxDurationSeconds: Int = 120,

    @SerializedName("expiry_seconds")
    val expirySeconds: Int = 600,

    @SerializedName("require_signed_urls")
    val requireSignedUrls: Boolean = false,

    val meta: Map<String, String>? = null
)