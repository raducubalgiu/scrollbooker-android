package com.example.scrollbooker.feature.myBusiness.data.mappers

import com.example.scrollbooker.feature.myBusiness.data.remote.schedules.ScheduleDto
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule

fun ScheduleDto.toDomain(): Schedule {
    return Schedule(
        id = id,
        dayOfWeek = dayOfWeek,
        startTime = startTime,
        endTime = endTime
    )
}