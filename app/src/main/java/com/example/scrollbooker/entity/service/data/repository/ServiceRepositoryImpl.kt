package com.example.scrollbooker.entity.service.data.repository
import com.example.scrollbooker.entity.service.data.mappers.toDomain
import com.example.scrollbooker.entity.service.data.remote.AttachManyServicesRequest
import com.example.scrollbooker.entity.service.data.remote.ServicesApiService
import com.example.scrollbooker.entity.service.domain.model.Service
import com.example.scrollbooker.entity.service.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api: ServicesApiService
): ServiceRepository {
    override suspend fun getServicesByBusinessId(businessId: Int): Result<List<Service>> = runCatching {
        api.getServicesByBusinessId(businessId).map { it.toDomain() }
    }

    override suspend fun getServicesByBusinessType(businessTypeId: Int): Result<List<Service>> = runCatching {
        api.getServicesByBusinessTypeId(businessTypeId).map { it.toDomain() }
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