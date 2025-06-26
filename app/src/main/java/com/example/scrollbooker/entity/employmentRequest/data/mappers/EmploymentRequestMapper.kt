package com.example.scrollbooker.entity.employmentRequest.data.mappers
import com.example.scrollbooker.entity.employmentRequest.data.remote.EmploymentRequestDto
import com.example.scrollbooker.entity.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.profession.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain

fun EmploymentRequestDto.toDomain(): EmploymentRequest {
    return EmploymentRequest(
        id = id,
        status = status,
        employee = employee.toDomain(),
        employer = employer.toDomain(),
        profession = profession.toDomain()
    )
}