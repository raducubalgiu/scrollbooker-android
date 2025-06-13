package com.example.scrollbooker.feature.myBusiness.employmentRequests.data.mappers

import com.example.scrollbooker.feature.myBusiness.employmentRequests.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.EmploymentRequestCreate

fun EmploymentRequestCreate.toDto(): EmploymentRequestCreateDto {
    return EmploymentRequestCreateDto(
        employeeId = employeeId,
        professionId = professionId,
        consentId = consentId
    )
}