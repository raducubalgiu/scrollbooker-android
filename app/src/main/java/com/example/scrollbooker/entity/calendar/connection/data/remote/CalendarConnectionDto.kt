package com.example.scrollbooker.entity.calendar.connection.data.remote

import com.google.gson.annotations.SerializedName

data class CalendarConnectionDto(
    val id: Int,
    val provider: String,
    val status: String,

    @SerializedName("google_account_email")
    val googleAccountEmail: String?,

    @SerializedName("last_synced_at")
    val lastSyncedAt: String?
)

data class ConnectGoogleCalendarRequest(
    @SerializedName("server_auth_code")
    val serverAuthCode: String
)
