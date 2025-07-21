package com.example.scrollbooker.entity.nomenclature.profession.data.mappers

import com.example.scrollbooker.entity.nomenclature.profession.data.remote.ProfessionDto
import com.example.scrollbooker.entity.nomenclature.profession.domain.model.Profession

fun ProfessionDto.toDomain(): Profession {
    return Profession(
        id = id,
        name = name,
        businessDomainId = businessDomainId
    )
}