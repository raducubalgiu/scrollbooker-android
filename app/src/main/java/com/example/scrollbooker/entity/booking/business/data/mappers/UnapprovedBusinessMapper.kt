package com.example.scrollbooker.entity.booking.business.data.mappers

import com.example.scrollbooker.entity.booking.business.data.remote.UnapprovedBusinessDataDto
import com.example.scrollbooker.entity.booking.business.data.remote.UnapprovedBusinessDto
import com.example.scrollbooker.entity.booking.business.data.remote.UnapprovedBusinessTypeDto
import com.example.scrollbooker.entity.booking.business.data.remote.UnapprovedLocationDto
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusiness
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusinessData
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedBusinessType
import com.example.scrollbooker.entity.booking.business.domain.model.UnapprovedLocation

fun UnapprovedBusinessDto.toDomain(): UnapprovedBusiness {
    return UnapprovedBusiness(
        id = this.id,
        fullName = this.fullName,
        username = this.username,
        avatar = this.avatar,
        business = business.toDomain()
    )
}

fun UnapprovedBusinessDataDto.toDomain(): UnapprovedBusinessData {
    return UnapprovedBusinessData(
        id = this.id,
        hasEmployees = this.hasEmployees,
        location = location.toDomain(),
        businessType = businessType.toDomain()
    )
}

fun UnapprovedLocationDto.toDomain(): UnapprovedLocation {
    return UnapprovedLocation(
        coordinates = this.coordinates,
        address = this.address
    )
}

fun UnapprovedBusinessTypeDto.toDomain(): UnapprovedBusinessType {
    return UnapprovedBusinessType(
        id = this.id,
        name = this.name
    )
}