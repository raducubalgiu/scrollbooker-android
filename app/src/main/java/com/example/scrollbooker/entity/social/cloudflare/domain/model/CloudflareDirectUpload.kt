package com.example.scrollbooker.entity.social.cloudflare.domain.model

import org.threeten.bp.LocalDateTime

data class CloudflareDirectUpload(
    val providerUid: String,
    val uploadUrl: String,
    val watermark: String?,
    val scheduledDeletion: LocalDateTime?
)