package com.example.scrollbooker.entity.social.cloudflare.data.remote

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class CloudflareDirectUploadDto(
    @SerializedName("provider_uid")
    val providerUid: String,

    @SerializedName("upload_url")
    val uploadUrl: String,

    val watermark: String?,

    @SerializedName("scheduled_deletion")
    val scheduledDeletion: LocalDateTime?,
)