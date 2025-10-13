package com.example.scrollbooker.entity.nomenclature.businessDomain.data.remote
import retrofit2.http.GET

interface BusinessDomainApiService {
    @GET("business-domains")
    suspend fun getAllBusinessDomains(): List<BusinessDomainDto>

    @GET("business-domains/with-business-types")
    suspend fun getAllBusinessDomainsWithBusinessTypes(): List<BusinessDomainsWithBusinessTypesDto>
}