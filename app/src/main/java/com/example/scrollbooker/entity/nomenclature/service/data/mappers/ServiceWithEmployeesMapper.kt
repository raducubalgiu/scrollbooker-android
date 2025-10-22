package com.example.scrollbooker.entity.nomenclature.service.data.mappers
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceEmployeeDto
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceWithEmployeesDto
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceEmployee
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceWithEmployees

fun ServiceWithEmployeesDto.toDomain(): ServiceWithEmployees {
    return ServiceWithEmployees(
        service = service.toDomain(),
        productsCount = productsCount,
        employees = employees.map { it.toDomain() }
    )
}

fun ServiceEmployeeDto.toDomain(): ServiceEmployee {
    return ServiceEmployee(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        profession = profession,
        ratingsAverage = ratingsAverage
    )
}