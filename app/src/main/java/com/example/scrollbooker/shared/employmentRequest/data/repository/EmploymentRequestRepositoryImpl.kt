package com.example.scrollbooker.shared.employmentRequest.data.repository
import com.example.scrollbooker.shared.employmentRequest.data.mappers.toDomain
import com.example.scrollbooker.shared.employmentRequest.data.remote.EmploymentRequestCreateDto
import com.example.scrollbooker.shared.employmentRequest.data.remote.EmploymentRequestsApiService
import com.example.scrollbooker.shared.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.shared.employmentRequest.domain.repository.EmploymentRequestRepository
import javax.inject.Inject

class EmploymentRequestRepositoryImpl @Inject constructor(
    private val apiService: EmploymentRequestsApiService
): EmploymentRequestRepository {
    override suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest> {
        return apiService.getUserEmploymentRequests(userId).map { it.toDomain() }
    }

    override suspend fun createEmploymentRequest(requestCreateDto: EmploymentRequestCreateDto) {
        return apiService.createEmploymentRequest(request = requestCreateDto)
    }

}