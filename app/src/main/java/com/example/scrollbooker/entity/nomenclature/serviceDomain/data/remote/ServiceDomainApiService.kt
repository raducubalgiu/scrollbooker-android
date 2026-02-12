package com.example.scrollbooker.entity.nomenclature.serviceDomain.data.remote
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceDomainApiService {
    @GET("/business-domains/{businessDomainId}/service-domains")
    suspend fun getAllServiceDomainsByBusinessDomain(
        @Path("businessDomainId") businessDomainId: Int
    ): List<ServiceDomainDto>

    @GET("/businesses/{businessId}/services-domains")
    suspend fun getAllServiceDomainsWithServicesByBusinessId(
        @Path("businessId") businessId: Int
    ): List<ServiceDomainWithServicesDto>
}