package com.example.scrollbooker.entity.booking.employmentRequest.data.repository
import com.example.scrollbooker.core.enums.EmploymentRequestStatusEnum
import com.example.scrollbooker.entity.booking.employmentRequest.data.mappers.toDomain
import com.example.scrollbooker.entity.booking.employmentRequest.data.mappers.toDto
import com.example.scrollbooker.entity.booking.employmentRequest.data.remote.EmploymentRequestRespondDto
import com.example.scrollbooker.entity.booking.employmentRequest.data.remote.EmploymentRequestsApiService
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequest
import com.example.scrollbooker.entity.booking.employmentRequest.domain.model.EmploymentRequestCreate
import com.example.scrollbooker.entity.booking.employmentRequest.domain.repository.EmploymentRequestRepository
import javax.inject.Inject

class EmploymentRequestRepositoryImpl @Inject constructor(
    private val apiService: EmploymentRequestsApiService
): EmploymentRequestRepository {
    override suspend fun getUserEmploymentRequests(userId: Int): List<EmploymentRequest> {
        return apiService.getUserEmploymentRequests(userId).map { it.toDomain() }
    }

    override suspend fun createEmploymentRequest(request: EmploymentRequestCreate) {
        val requestDto = request.toDto()
        return apiService.createEmploymentRequest(requestDto)
    }

    override suspend fun responseEmploymentRequest(
        status: EmploymentRequestStatusEnum,
        employmentId: Int
    ) {
        val request = EmploymentRequestRespondDto(status.key)
        return apiService.respondEmploymentRequest(request, employmentId)
    }

}