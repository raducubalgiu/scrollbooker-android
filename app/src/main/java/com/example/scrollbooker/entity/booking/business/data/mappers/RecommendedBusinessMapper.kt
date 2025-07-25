package com.example.scrollbooker.entity.booking.business.data.mappers
import com.example.scrollbooker.entity.booking.business.data.remote.RecommendedBusinessDto
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.user.userSocial.data.mappers.toDomain

fun RecommendedBusinessDto.toDomain(): RecommendedBusiness {
    return RecommendedBusiness(
        user = user.toDomain(),
        distance = distance,
        isOpen = isOpen
    )
}