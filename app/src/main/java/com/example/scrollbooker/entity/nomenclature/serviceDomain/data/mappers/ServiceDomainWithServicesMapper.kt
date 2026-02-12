package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers

import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainServiceDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.ServiceDomainWithServicesDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainService
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.ServiceDomainWithServices

fun ServiceDomainWithServicesDto.toDomain(): ServiceDomainWithServices {
    return ServiceDomainWithServices(
        id = id,
        name = name,
        services = services.map { it.toDomain() }
    )
}

fun ServiceDomainServiceDto.toDomain(): ServiceDomainService {
    return ServiceDomainService(
        id = id,
        name = name,
        shortName = shortName,
        isSelected = isSelected
    )
}