package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers

import com.example.scrollbooker.entity.nomenclature.filter.data.mapper.toDomain
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainWithEmployeeServicesDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceEmployeeDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceWithEmployeesDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithEmployeeServices
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceEmployee
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceWithEmployees

fun ServiceDomainWithEmployeeServicesDto.toDomain(): ServiceDomainWithEmployeeServices {
    return ServiceDomainWithEmployeeServices(
        id = id,
        name = name,
        services = services.map { it.toDomain() }
    )
}

fun ServiceWithEmployeesDto.toDomain(): ServiceWithEmployees {
    return ServiceWithEmployees(
        id = id,
        name = name,
        shortName = shortName,
        filters = filters.map { it.toDomain() },
        employees = employees.map { it.toDomain() },
        productsCount = productsCount
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