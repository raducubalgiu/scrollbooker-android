package com.example.scrollbooker.entity.businessDomain.data.remote
import retrofit2.http.GET

interface BusinessDomainApiService {
    @GET("business-domains")
    suspend fun getAllBusinessDomains(): List<BusinessDomainDto>
}