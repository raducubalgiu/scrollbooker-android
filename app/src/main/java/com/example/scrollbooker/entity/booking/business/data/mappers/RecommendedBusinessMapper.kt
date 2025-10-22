package com.example.scrollbooker.entity.booking.business.data.mappers
import com.example.scrollbooker.entity.booking.business.data.remote.RecommendedBusinessDto
import com.example.scrollbooker.entity.booking.business.data.remote.RecommendedBusinessUserDto
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusinessUser

fun RecommendedBusinessDto.toDomain(): RecommendedBusiness {
    return RecommendedBusiness(
        user = user.toDomain(),
        distance = distance,
        isOpen = isOpen
    )
}

fun RecommendedBusinessUserDto.toDomain(): RecommendedBusinessUser {
    return RecommendedBusinessUser(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        profession = profession,
        ratingsAverage = ratingsAverage
    )
}