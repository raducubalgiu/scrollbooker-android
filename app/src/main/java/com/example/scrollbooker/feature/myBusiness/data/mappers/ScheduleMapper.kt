package com.example.scrollbooker.feature.myBusiness.data.mappers

import com.example.scrollbooker.feature.myBusiness.data.remote.schedules.ScheduleDto
import com.example.scrollbooker.feature.myBusiness.domain.model.Schedule

fun ScheduleDto.toDomain(): Schedule {
    return Schedule(
        id = id,
        dayOfWeek = dayOfWeek,
        userId = userId,
        businessId = businessId,
        startTime = startTime,
        endTime = endTime,
        dayWeekNumber = dayWeekNumber
    )
}