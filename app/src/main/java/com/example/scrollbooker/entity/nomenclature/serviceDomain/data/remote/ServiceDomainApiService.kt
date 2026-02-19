package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceDomainApiService {
    @GET("/business-domains/{businessDomainId}/service-domains")
    suspend fun getAllServiceDomainsByBusinessDomain(
        @Path("businessDomainId") businessDomainId: Int
    ): List<ServiceDomainDto>

    @GET("/businesses/{businessId}/service-domains")
    suspend fun getSelectedServiceDomainsWithServicesByBusinessId(
        @Path("businessId") businessId: Int
    ): List<SelectedServiceDomainsWithServicesDto>

    @GET("/users/{userId}/service-domains")
    suspend fun getAllServiceDomainsWithServicesByUserId(
        @Path("userId") userId: Int,
        @Query("only_with_products") onlyWithProducts: Boolean,
        @Query("with_filters") withFilters: Boolean
    ): List<ServiceDomainWithEmployeeServicesDto>
}