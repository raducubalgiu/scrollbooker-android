package com.example.scrollbooker.entity.booking.availability.data.mappers

import com.example.scrollbooker.entity.booking.availability.data.remote.EmployeeAvailabilityDto
import com.example.scrollbooker.entity.booking.availability.domain.model.EmployeeAvailability

fun EmployeeAvailabilityDto.toDomain(): EmployeeAvailability = EmployeeAvailability(
    employeeId = employeeId,
    hasAvailability = hasAvailability
)
