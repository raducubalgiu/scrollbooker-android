package com.example.scrollbooker.entity.nomenclature.service.data.remote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServicesApiService {
    @GET("businesses/{businessId}/services")
    suspend fun getServicesByBusinessId(
        @Path("businessId") businessId: Int
    ): List<ServiceDto>

    @GET("service-domains/{serviceDomainId}/services")
    suspend fun getServicesByServiceDomainId(
        @Path("serviceDomainId") serviceDomainId: Int
    ): List<ServiceWithFiltersDto>

    @GET("users/{userId}/services")
    suspend fun getServicesByUserId(
        @Path("userId") userId: Int,
        @Query("only_with_products") onlyWithProducts: Boolean
    ): List<ServiceDomainWithServicesDto>
}