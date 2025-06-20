package com.example.scrollbooker.shared.employmentRequest.data.mappers
import com.example.scrollbooker.shared.employmentRequest.data.remote.EmploymentRequestDto
import com.example.scrollbooker.shared.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.shared.profession.data.mappers.toDomain
import com.example.scrollbooker.shared.user.userSocial.data.mappers.toDomain

fun EmploymentRequestDto.toDomain(): EmploymentRequest {
    return EmploymentRequest(
        id = id,
        status = status,
        employee = employee.toDomain(),
        employer = employer.toDomain(),
        profession = profession.toDomain()
    )
}