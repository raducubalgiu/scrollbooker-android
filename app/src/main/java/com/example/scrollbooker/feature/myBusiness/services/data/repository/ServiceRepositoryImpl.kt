package com.example.scrollbooker.feature.myBusiness.services.data.repository

import com.example.scrollbooker.feature.myBusiness.services.data.mappers.toDomain
import com.example.scrollbooker.feature.myBusiness.services.data.remote.ServicesApiService
import com.example.scrollbooker.feature.myBusiness.services.domain.model.Service
import com.example.scrollbooker.feature.myBusiness.services.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api: ServicesApiService
): ServiceRepository {
    override suspend fun getServices(userId: Int): List<Service> {
        return api.getServices(userId).map { it.toDomain() }
    }

}