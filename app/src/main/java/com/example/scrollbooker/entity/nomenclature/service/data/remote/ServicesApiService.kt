package com.example.scrollbooker.entity.nomenclature.service.data.remote
import retrofit2.http.GET
import retrofit2.http.Path

interface ServicesApiService {
    @GET("businesses/{businessId}/services")
    suspend fun getServicesByBusinessId(
        @Path("businessId") businessId: Int
    ): List<ServiceDto>

    @GET("business-types/{businessTypeId}/services")
    suspend fun getServicesByBusinessTypeId(
        @Path("businessTypeId") businessTypeId: Int
    ): List<ServiceDto>

    @GET("service-domains/{serviceDomainId}/services")
    suspend fun getServicesByServiceDomainId(
        @Path("serviceDomainId") serviceDomainId: Int
    ): List<ServiceDto>

    @GET("users/{userId}/services")
    suspend fun getServicesByUserId(
        @Path("userId") userId: Int
    ): List<ServiceDomainWithServicesDto>
}