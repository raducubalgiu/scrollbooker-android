package com.example.scrollbooker.entity.booking.business.data.repository

import com.example.scrollbooker.entity.auth.data.mappers.toDomain
import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessApiService
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessCreateDto
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessHasEmployeesUpdateRequest
import com.example.scrollbooker.entity.booking.business.data.remote.BusinessServicesUpdateRequest
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse
import com.example.scrollbooker.entity.booking.business.domain.model.RecommendedBusiness
import com.example.scrollbooker.entity.booking.business.domain.repository.BusinessRepository
import javax.inject.Inject

class BusinessRepositoryImpl @Inject constructor(
    private val apiService: BusinessApiService
): BusinessRepository {
    override suspend fun searchBusinessAddress(query: String): List<BusinessAddress> {
        return apiService.searchBusinessAddress(query).map { it.toDomain() }
    }

    override suspend fun updateBusinessServices(serviceIds: List<Int>): AuthState {
        val request = BusinessServicesUpdateRequest(serviceIds)

        return apiService.updateBusinessServices(request).toDomain()
    }

    override suspend fun getBusiness(userId: Int): Business {
        return apiService.getBusinessByUserId(userId).toDomain()
    }

    override suspend fun getBusinessById(businessId: Int): Business {
        return apiService.getBusinessById(businessId).toDomain()
    }

    override suspend fun getRecommendedBusinesses(lng: Float?, lat: Float?, timezone: String): List<RecommendedBusiness> {
        return apiService.getRecommendedBusinesses(lng, lat, timezone).map { it.toDomain() }
    }

    override suspend fun updateBusinessHasEmployees(hasEmployees: Boolean): AuthState {
        val request = BusinessHasEmployeesUpdateRequest(hasEmployees)

        return apiService.updateBusinessHasEmployees(request).toDomain()
    }

    override suspend fun createBusiness(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String
    ): BusinessCreateResponse {
        val request = BusinessCreateDto(
            description = description,
            placeId = placeId,
            businessTypeId = businessTypeId,
            ownerFullName = ownerFullName
        )
        return apiService.createBusiness(request).toDomain()
    }
}