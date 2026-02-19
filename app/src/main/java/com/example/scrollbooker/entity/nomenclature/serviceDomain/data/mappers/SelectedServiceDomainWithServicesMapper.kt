package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.mappers

import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.SelectedServiceDomainsWithServicesDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote.SelectedServiceDto
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedService
import com.example.scrollbooker.entity.nomenclature.serviceDomain.domain.model.SelectedServiceDomainsWithServices

fun SelectedServiceDomainsWithServicesDto.toDomain(): SelectedServiceDomainsWithServices {
    return SelectedServiceDomainsWithServices(
        id = id,
        name = name,
        services = services.map { it.toDomain() }
    )
}

fun SelectedServiceDto.toDomain(): SelectedService {
    return SelectedService(
        id = id,
        name = name,
        shortName = shortName,
        isSelected = isSelected
    )
}