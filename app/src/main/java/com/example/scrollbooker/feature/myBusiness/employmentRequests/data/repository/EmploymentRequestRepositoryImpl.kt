package com.example.scrollbooker.feature.myBusiness.employmentRequests.data.repository

import com.example.scrollbooker.feature.myBusiness.employmentRequests.data.mappers.toDomain
import com.example.scrollbooker.feature.myBusiness.employmentRequests.data.remote.EmploymentRequestsApiService
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.model.EmploymentRequest
import com.example.scrollbooker.feature.myBusiness.employmentRequests.domain.repository.EmploymentRequestRepository
import javax.inject.Inject

class EmploymentRequestRepositoryImpl @Inject constructor(
    private val apiService: EmploymentRequestsApiService
): EmploymentRequestRepository {
    override suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest> {
        return apiService.getUserEmploymentRequests(userId).map { it.toDomain() }
    }

}