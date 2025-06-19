package com.example.scrollbooker.shared.consent.data.mappers

import com.example.scrollbooker.shared.consent.data.remote.ConsentDto
import com.example.scrollbooker.shared.consent.domain.model.Consent

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