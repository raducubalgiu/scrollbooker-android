package com.example.scrollbooker.entity.calendar.connection.data.mappers

import com.example.scrollbooker.entity.calendar.connection.data.remote.CalendarConnectionDto
import com.example.scrollbooker.entity.calendar.connection.domain.model.CalendarConnection

fun CalendarConnectionDto.toDomain(): CalendarConnection = CalendarConnection(
    id = id,
    provider = provider,
    status = status,
    googleAccountEmail = googleAccountEmail,
    lastSyncedAt = lastSyncedAt
)
