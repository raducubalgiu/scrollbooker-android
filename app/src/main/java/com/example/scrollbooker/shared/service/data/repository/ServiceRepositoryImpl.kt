package com.example.scrollbooker.shared.service.data.repository
import com.example.scrollbooker.shared.service.data.mappers.toDomain
import com.example.scrollbooker.shared.service.data.remote.AttachManyServicesRequest
import com.example.scrollbooker.shared.service.data.remote.ServicesApiService
import com.example.scrollbooker.shared.service.domain.repository.ServiceRepository
import com.example.scrollbooker.shared.service.domain.model.Service
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