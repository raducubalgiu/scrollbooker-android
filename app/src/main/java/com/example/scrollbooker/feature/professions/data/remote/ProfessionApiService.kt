package com.example.scrollbooker.feature.professions.data.remote
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfessionsApiService {
    @GET("business-types/{businessTypeId}/professions")
    suspend fun getProfessionsByBusinessType(
        @Path("businessTypeId") businessTypeId: Int,
    ): List<ProfessionDto>
}