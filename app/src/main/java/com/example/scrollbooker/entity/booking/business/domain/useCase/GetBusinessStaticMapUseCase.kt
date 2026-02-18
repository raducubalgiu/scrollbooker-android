package com.example.scrollbooker.entity.booking.business.domain.useCase

import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository

class GetBusinessStaticMapUseCase(
    private val repository: BusinessRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lng: Double,
        zoom: Int = 13,
    ): Result<ByteArray> {
        return repository.getStaticMap(lat, lng, zoom)
    }
}