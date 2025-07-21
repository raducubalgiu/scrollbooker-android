package com.example.scrollbooker.entity.booking.business.domain.repository

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.booking.business.domain.model.Business
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessAddress
import com.example.scrollbooker.entity.booking.business.domain.model.BusinessCreateResponse

interface BusinessRepository {
    suspend fun searchBusinessAddress(query: String): List<BusinessAddress>
    suspend fun updateBusinessServices(serviceIds: List<Int>): AuthState
    suspend fun getBusiness(userId: Int): Business
    suspend fun getBusinessById(businessId: Int): Business
    suspend fun updateBusinessHasEmployees(hasEmployees: Boolean): AuthState
    suspend fun createBusiness(
        description: String?,
        placeId: String,
        businessTypeId: Int,
        ownerFullName: String
    ): BusinessCreateResponse
}