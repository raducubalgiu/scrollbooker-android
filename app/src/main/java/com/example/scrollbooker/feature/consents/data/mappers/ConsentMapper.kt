package com.example.scrollbooker.feature.consents.data.mappers

import com.example.scrollbooker.feature.consents.data.remote.ConsentDto
import com.example.scrollbooker.feature.consents.domain.model.Consent

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