package com.example.scrollbooker.entity.business.domain.repository

import com.example.scrollbooker.entity.business.domain.model.BusinessAddress

interface BusinessRepository {
    suspend fun searchBusinessAddress(query: String): List<BusinessAddress>
}