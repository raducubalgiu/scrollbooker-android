package com.example.scrollbooker.shared.businessType.data.mappers
import com.example.scrollbooker.shared.businessType.data.remote.BusinessTypeDto
import com.example.scrollbooker.shared.businessType.domain.model.BusinessType

fun BusinessTypeDto.toDomain(): BusinessType {
    return BusinessType(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}