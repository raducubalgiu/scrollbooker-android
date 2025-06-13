package com.example.scrollbooker.feature.myBusiness.employmentRequests.data.mappers
import com.example.scrollbooker.feature.myBusiness.employmentRequests.data.remote.EmploymentRequestDto
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.EmploymentRequest
import com.example.scrollbooker.feature.professions.data.mappers.toDomain
import com.example.scrollbooker.feature.userSocial.data.mappers.toDomain

fun EmploymentRequestDto.toDomain(): EmploymentRequest {
    return EmploymentRequest(
        id = id,
        status = status,
        employee = employee.toDomain(),
        employer = employer.toDomain(),
        profession = profession.toDomain()
    )
}