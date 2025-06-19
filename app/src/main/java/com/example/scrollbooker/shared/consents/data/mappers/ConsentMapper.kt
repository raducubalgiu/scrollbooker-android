package com.example.scrollbooker.shared.consents.data.mappers

import com.example.scrollbooker.shared.consents.data.remote.ConsentDto
import com.example.scrollbooker.shared.consents.domain.model.Consent

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