package com.example.scrollbooker.screens.profile.settings.reportProblem.data.remote
import retrofit2.http.Body
import retrofit2.http.POST

interface ReportProblemApiService {
    @POST("/problems")
    suspend fun sendProblem(
        @Body request: ReportProblemRequest
    )
}