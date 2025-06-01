package com.example.scrollbooker.feature.myBusiness.data.repository
import com.example.scrollbooker.feature.myBusiness.data.mappers.toDomain
import com.example.scrollbooker.feature.myBusiness.data.remote.service.ServicesApiService
import com.example.scrollbooker.feature.myBusiness.domain.model.Service
import com.example.scrollbooker.feature.myBusiness.domain.repository.ServiceRepository
import javax.inject.Inject

class ServiceRepositoryImpl @Inject constructor(
    private val api: ServicesApiService
): ServiceRepository {
    override suspend fun getServices(userId: Int): List<Service> {
        return api.getServices(userId).map { it.toDomain() }
    }

}