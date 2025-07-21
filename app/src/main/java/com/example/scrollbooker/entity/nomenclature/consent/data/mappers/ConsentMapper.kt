package com.example.scrollbooker.entity.nomenclature.consent.data.mappers

import com.example.scrollbooker.entity.nomenclature.consent.data.remote.ConsentDto
import com.example.scrollbooker.entity.nomenclature.consent.domain.model.Consent

fun ConsentDto.toDomain(): Consent {
    return Consent(
        id = id,
        name = name,
        title = title,
        text = text,
        version = version,
        createdAt = createdAt
    )
}