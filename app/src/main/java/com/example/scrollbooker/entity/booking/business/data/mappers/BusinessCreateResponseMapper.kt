package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessCreateResponseDto
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse

fun BusinessCreateResponseDto.toDomain(): BusinessCreateResponse {
    return BusinessCreateResponse(
        authState = authState.toDomain(),
        businessId = businessId
    )
}