package com.example.scrollbooker.entity.nomenclature.service.data.mappers
import com.example.scrollbooker.entity.nomenclature.filter.data.mapper.toDomain
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceDomainWithServicesDto
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServiceEmployeeDto
import com.example.scrollbooker.entity.nomenclature.service.data.remote.ServicesWithEmployeesDto
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceDomainWithServices
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServiceEmployee
import com.example.scrollbooker.entity.nomenclature.service.domain.model.ServicesWithEmployees

fun ServiceDomainWithServicesDto.toDomain(): ServiceDomainWithServices {
    return ServiceDomainWithServices(
        serviceDomain = serviceDomain,
        services = services.map { it.toDomain() }
    )
}

fun ServicesWithEmployeesDto.toDomain(): ServicesWithEmployees {
    return ServicesWithEmployees(
        service = service.toDomain(),
        subFilters = subFilters.map { it.toDomain() },
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