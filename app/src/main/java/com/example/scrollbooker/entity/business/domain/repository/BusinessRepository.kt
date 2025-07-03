package com.example.scrollbooker.entity.business.domain.repository

import com.example.scrollbooker.entity.auth.domain.model.AuthState
import com.example.scrollbooker.entity.business.domain.model.BusinessAddress

interface BusinessRepository {
    suspend fun searchBusinessAddress(query: String): List<BusinessAddress>
    suspend fun updateBusinessServices(serviceIds: List<Int>): AuthState
}