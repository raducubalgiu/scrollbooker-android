package com.example.scrollbooker.entity.profession.data.mappers

import com.example.scrollbooker.entity.profession.data.remote.ProfessionDto
import com.example.scrollbooker.entity.profession.domain.model.Profession

fun ProfessionDto.toDomain(): Profession {
    return Profession(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}