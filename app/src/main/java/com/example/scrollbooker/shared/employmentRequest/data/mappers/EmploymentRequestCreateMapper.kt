package com.example.scrollbooker.shared.employmentRequest.data.mappers

import com.example.scrollbooker.shared.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.shared.employmentRequest.domain.model.EmploymentRequestCreate

fun EmploymentRequestCreate.toDto(): EmploymentRequestCreateDto {
    return EmploymentRequestCreateDto(
        employeeId = employeeId,
        professionId = professionId,
        consentId = consentId
    )
}