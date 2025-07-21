package com.example.scrollbooker.entity.booking.schedule.data.mappers

import com.example.scrollbooker.entity.booking.schedule.data.remote.ScheduleDto
import com.example.scrollbooker.entity.booking.schedule.domain.model.Schedule

fun ScheduleDto.toDomain(): Schedule {
    return Schedule(
        id = id,
        dayOfWeek = dayOfWeek,
        startTime = startTime,
        endTime = endTime
    )
}

fun Schedule.toDto(): ScheduleDto {
    return ScheduleDto(
        id = id,
        dayOfWeek = dayOfWeek,
        startTime = startTime,
        endTime = endTime
    )
}

fun List<ScheduleDto>.toDomain(): List<Schedule> = map { it.toDomain() }
fun List<Schedule>.toDto(): List<ScheduleDto> = map { it.toDto() }