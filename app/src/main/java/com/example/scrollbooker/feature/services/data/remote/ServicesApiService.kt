package com.example.scrollbooker.feature.services.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ServicesApiService {
    @GET("users/{userId}/services")
    suspend fun getServices(
        @Path("userId") userId: Int
    ): List<ServiceDto>

    @GET("business-types/{businessTypeId}/services")
    suspend fun getServicesByBusinessType(
        @Path("businessTypeId") businessTypeId: Int
    ): List<ServiceDto>

    @POST("/businesses/{businessId}/services")
    suspend fun attachManyService(
        @Path("businessId") businessId: Int,
        @Body request: AttachManyServicesRequest
    )

    @DELETE("businesses/{businessId}/services/{serviceId}")
    suspend fun detachService(
        @Path("businessId") businessId: Int,
        @Path("serviceId") serviceId: Int
    )
}