package com.example.scrollbooker.shared.professions.data.mappers

import com.example.scrollbooker.shared.professions.data.remote.ProfessionDto
import com.example.scrollbooker.shared.professions.domain.model.Profession

fun ProfessionDto.toDomain(): Profession {
    return Profession(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}