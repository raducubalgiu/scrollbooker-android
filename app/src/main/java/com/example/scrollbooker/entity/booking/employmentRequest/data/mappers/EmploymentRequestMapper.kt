package com.example.scrollbooker.entity.booking.employmentRequest.data.mappers
import com.example.scrollbooker.entity.booking.employmentRequest.data.remote.EmploymentRequestDto
import com.example.scrollbooker.entity.booking.employmentRequest.data.remote.EmploymentRequestUserDto
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequestUser
import com.example.scrollbooker.entity.nomenclature.profession.data.mappers.toDomain
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain
import org.threeten.bp.ZonedDateTime

fun EmploymentRequestDto.toDomain(): EmploymentRequest {
    return EmploymentRequest(
        id = id,
        createdAt = ZonedDateTime.parse(createdAt),
        status = status,
        employee = employee.toDomain(),
        employer = employer.toDomain(),
        profession = profession.toDomain()
    )
}

fun EmploymentRequestUserDto.toDomain(): EmploymentRequestUser {
    return EmploymentRequestUser(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        profession = profession
    )
}