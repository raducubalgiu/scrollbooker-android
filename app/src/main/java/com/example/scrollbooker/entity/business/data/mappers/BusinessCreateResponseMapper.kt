package com.example.scrollbooker.entity.business.data.mappers

import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.business.data.remote.BusinessCreateResponseDto
import com.example.scrollbooker.entity.business.domain.model.BusinessCreateResponse

fun BusinessCreateResponseDto.toDomain(): BusinessCreateResponse {
    return BusinessCreateResponse(
        authState = authState.toDomain(),
        businessId = businessId
    )
}