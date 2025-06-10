package com.example.scrollbooker.feature.myBusiness.employeeDismissal.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface EmployeesDismissalApiService {
    @GET("consents/{consentName}")
    suspend fun getEmployeeDismissalConsent(
        @Path("consentName") consentName: String,
    ): EmployeeDismissalDto
}