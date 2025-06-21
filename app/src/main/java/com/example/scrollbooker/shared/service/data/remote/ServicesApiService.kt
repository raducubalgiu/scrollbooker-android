package com.example.scrollbooker.shared.service.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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