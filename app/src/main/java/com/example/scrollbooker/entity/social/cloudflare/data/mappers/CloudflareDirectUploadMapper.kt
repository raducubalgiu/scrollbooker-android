package com.example.scrollbooker.entity.social.cloudflare.data.mappers

import com.example.scrollbooker.entity.social.cloudflare.data.remote.CloudflareDirectUploadDto
import com.example.scrollbooker.entity.social.cloudflare.domain.model.CloudflareDirectUpload

fun CloudflareDirectUploadDto.toDomain(): CloudflareDirectUpload {
    return CloudflareDirectUpload(
        providerUid = providerUid,
        uploadUrl = uploadUrl,
        watermark = watermark,
        scheduledDeletion = scheduledDeletion
    )
}