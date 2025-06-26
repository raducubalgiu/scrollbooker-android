package com.example.scrollbooker.entity.employmentRequest.data.mappers

import com.example.scrollbooker.entity.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.entity.employmentRequest.domain.model.EmploymentRequestCreate

fun EmploymentRequestCreate.toDto(): EmploymentRequestCreateDto {
    return EmploymentRequestCreateDto(
        employeeId = employeeId,
        professionId = professionId,
        consentId = consentId
    )
}