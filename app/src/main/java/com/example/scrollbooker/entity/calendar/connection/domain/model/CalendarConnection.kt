package com.example.scrollbooker.entity.calendar.connection.domain.model

data class CalendarConnection(
    val id: Int,
    val provider: String,
    val status: String,
    val googleAccountEmail: String?,
    val lastSyncedAt: String?
)

fun CalendarConnection.isActive(): Boolean = status.equals("ACTIVE", ignoreCase = true)
