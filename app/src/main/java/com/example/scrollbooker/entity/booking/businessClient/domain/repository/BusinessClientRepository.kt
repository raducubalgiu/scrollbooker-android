package com.example.scrollbooker.entity.booking.businessClient.domain.repository

import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClient
import com.example.scrollbooker.entity.booking.businessClient.domain.model.BusinessClientCreate

interface BusinessClientRepository {
    suspend fun getBusinessClients(businessId: Int, query: String?): List<BusinessClient>
    suspend fun createBusinessClient(businessId: Int, request: BusinessClientCreate): BusinessClient
}
