package com.example.scrollbooker.entity.nomenclature.businessDomain.data.mappers

import com.example.scrollbooker.entity.nomenclature.businessDomain.data.remote.BusinessDomainsWithBusinessTypesDto
import com.example.scrollbooker.entity.nomenclature.businessDomain.domain.model.BusinessDomainsWithBusinessTypes

fun BusinessDomainsWithBusinessTypesDto.toDomain(): BusinessDomainsWithBusinessTypes {
    return BusinessDomainsWithBusinessTypes(
        id = id,
        name = name,
        shortName = shortName,
        businessTypes = businessTypes,
    )
}