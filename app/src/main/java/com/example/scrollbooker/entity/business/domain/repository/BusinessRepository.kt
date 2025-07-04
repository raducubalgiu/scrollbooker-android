package com.example.scrollbooker.entity.business.domain.repository

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.business.domain.model.Business
import com.example.scrollbooker.entity.business.domain.model.BusinessAddress

interface BusinessRepository {
    suspend fun searchBusinessAddress(query: String): List<BusinessAddress>
    suspend fun updateBusinessServices(serviceIds: List<Int>): AuthState
    suspend fun getBusiness(userId: Int): Business
    suspend fun updateBusinessHasEmployees(hasEmployees: Boolean): AuthState
}