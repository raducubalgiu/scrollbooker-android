package com.example.scrollbooker.feature.professions.data.mappers

import com.example.scrollbooker.feature.professions.data.remote.ProfessionDto
import com.example.scrollbooker.feature.professions.domain.model.Profession

fun ProfessionDto.toDomain(): Profession {
    return Profession(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}