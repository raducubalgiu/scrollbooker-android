package com.example.scrollbooker.entity.booking.employmentRequest.data.mappers

import com.example.scrollbooker.entity.booking.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequestCreate

fun EmploymentRequestCreate.toDto(): EmploymentRequestCreateDto {
    return EmploymentRequestCreateDto(
        employeeId = employeeId,
        professionId = professionId,
        consentId = consentId
    )
}