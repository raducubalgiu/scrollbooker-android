package com.example.scrollbooker.entity.social.cloudflare.data.remote

import com.google.gson.annotations.SerializedName

data class CloudflareVideoDetailsDto(
    val uid: String,
    val thumbnail: String? = null,
    val preview: String? = null,

    @SerializedName("ready_to_stream")
    val readyToStream: Boolean = false,

    val status: CloudflareVideoStatusDto,
    val playback: CloudflarePlaybackDto? = null
)

data class CloudflareVideoStatusDto(
    val state: String? = null
)

data class CloudflarePlaybackDto(
    val hls: String? = null,
    val dash: String? = null
)