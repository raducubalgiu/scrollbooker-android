package com.example.scrollbooker.entity.nomenclature.service.data.mappers
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceWithEmployeesDto
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithEmployees
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain

fun ServiceWithEmployeesDto.toDomain(): ServiceWithEmployees {
    return ServiceWithEmployees(
        service = service.toDomain(),
        productsCount = productsCount,
        employees = employees.map { it.toDomain() }
    )
}