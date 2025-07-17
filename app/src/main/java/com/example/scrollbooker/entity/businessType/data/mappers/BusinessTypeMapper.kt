package com.example.scrollbooker.entity.businessType.data.mappers
import com.example.scrollbooker.entity.businessType.data.remote.BusinessTypeDto
import com.example.scrollbooker.entity.businessType.domain.model.BusinessType

fun BusinessTypeDto.toDomain(): BusinessType {
    return BusinessType(
        id = id,
        name = name,
        businessDomainId = businessDomainId,
        plural = plural
    )
}