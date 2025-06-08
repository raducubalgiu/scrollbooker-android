package com.example.scrollbooker.feature.myBusiness.services.data.repository

import com.example.scrollbooker.feature.myBusiness.services.data.mappers.toDomain
import com.example.scrollbooker.feature.myBusiness.services.data.remote.AttachManyServicesRequest
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

    override suspend fun getServicesByBusinessType(businessTypeId: Int): List<Service> {
        return api.getServicesByBusinessType(businessTypeId).map { it.toDomain() }
    }

    override suspend fun attachManyService(
        businessId: Int,
        serviceIds: List<Int>
    ) {
        val request = AttachManyServicesRequest(serviceIds)
        return api.attachManyService(businessId, request)
    }

    override suspend fun detachService(businessId: Int, serviceId: Int) {
        return api.detachService(businessId, serviceId)
    }
}