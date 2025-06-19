package com.example.scrollbooker.shared.profession.data.mappers

import com.example.scrollbooker.shared.profession.data.remote.ProfessionDto
import com.example.scrollbooker.shared.profession.domain.model.Profession

fun ProfessionDto.toDomain(): Profession {
    return Profession(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}