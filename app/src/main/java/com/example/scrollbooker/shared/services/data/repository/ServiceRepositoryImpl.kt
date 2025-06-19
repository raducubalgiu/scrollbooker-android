package com.example.scrollbooker.shared.services.data.repository
import com.example.scrollbooker.shared.services.data.mappers.toDomain
import com.example.scrollbooker.shared.services.data.remote.AttachManyServicesRequest
import com.example.scrollbooker.shared.services.data.remote.ServicesApiService
import com.example.scrollbooker.shared.services.domain.repository.ServiceRepository
import com.example.scrollbooker.shared.services.domain.model.Service
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